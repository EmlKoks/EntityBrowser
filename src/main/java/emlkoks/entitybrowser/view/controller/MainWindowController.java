package emlkoks.entitybrowser.view.controller;

import emlkoks.entitybrowser.Main;
import emlkoks.entitybrowser.connection.Connection;
import emlkoks.entitybrowser.connection.Provider;
import emlkoks.entitybrowser.query.FieldFilter;
import emlkoks.entitybrowser.query.QueryBuilder;
import emlkoks.entitybrowser.query.comparator.AbstractComparator;
import emlkoks.entitybrowser.query.comparator.ComparatorFactory;
import emlkoks.entitybrowser.query.comparator.ComparatorManager;
import emlkoks.entitybrowser.query.comparator.ComparatorNotFoundException;
import emlkoks.entitybrowser.query.comparator.expression.Expression;
import emlkoks.entitybrowser.session.Entity;
import emlkoks.entitybrowser.session.FieldProperty;
import emlkoks.entitybrowser.session.Session;
import emlkoks.entitybrowser.view.dialog.ErrorDialogCreator;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeSet;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javax.persistence.PersistenceException;
import lombok.extern.slf4j.Slf4j;


/**
 * Created by EmlKoks on 18.03.17.
 */
@Slf4j
public class MainWindowController implements Initializable {

    @FXML
    private BorderPane mainPane;

    @FXML
    private ChoiceBox<String> entities;

    @FXML
    private ChoiceBox<String> fields;

    @FXML
    private SplitPane centerContent;

    @FXML
    private ResourceBundle resources;

    @FXML
    private BorderPane filtersPane;

    @FXML
    private AnchorPane leftContent;

    @FXML
    private AnchorPane rightContent;

    @FXML
    private GridPane filtersGrid;

    private Session session;

    private Set<String> addedFilters = new TreeSet<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Main.setMainController(this);
        this.resources = resources;
        filtersPane.prefWidthProperty().bind(leftContent.widthProperty());
        filtersPane.prefHeightProperty().bind(leftContent.heightProperty());
        entities.valueProperty().addListener((observable, oldValue, newValue) -> {
            fields.getItems().clear();
            fields.getItems().addAll(session.getEntity(newValue).getFieldsNames());
            fields.setValue(fields.getItems().get(0));
            filtersGrid.getChildren().clear();
            addedFilters.clear();
        });
//        debug();
    }

    private void debug() {
        Connection connection = Main.savedConnections.getByName("ers");
        File lib = new File("/home/koks/Projekty/EntityBrowser/ERS-db-entities-1.1.jar");
        session = new Session(connection, lib, Provider.EclipseLink);
        if (session.connect()) {
            entities.getItems().addAll(session.getClassNames());
            centerContent.setDisable(false);
            validateEntities(session);
        }
    }

    //TODO validate in background
    private static void validateEntities(Session session) {
        session.getClassNames().stream()
                .map(clazz -> session.getEntity(clazz))
                .forEach(entity -> entity.getFields().stream()
                    .forEach(field -> {
                        try {
                            ComparatorManager.getExpressionByField(field.getField());
                        } catch (ComparatorNotFoundException e) {
                            System.out.println(e.getMessage());
                        }
                    }));
    }

    @FXML
    private void newConnection() {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/newConnection.fxml"), resources);
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene dialogScene = new Scene(loader.getRoot());
        NewConnectionController controller = loader.getController();
        stage.setTitle(resources.getString("newConnection.title"));
        stage.setScene(dialogScene);
        stage.showAndWait();
        Connection connection = controller.getConnection();
        if (connection != null) {
            createNewSessionTab(controller.getConnection());
        }
    }

    private void createNewSessionTab(Connection connection) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/entityLibraryChoice.fxml"), resources);
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        Scene dialogScene = new Scene(loader.getRoot());
        stage.setTitle(resources.getString("entityLibraryChoice.title"));
        stage.setScene(dialogScene);
        stage.showAndWait();
        EntityLibraryChoiceController controller = loader.getController();
        createSession(connection, controller.getEntityLibrary(), controller.getProvider());
    }

    private void createSession(Connection connection, File file, Provider provider) {
        try {
            session = new Session(connection, file, provider);
            session.connect();
        } catch (PersistenceException ex) {
            ex.printStackTrace();

            new ErrorDialogCreator(
                    "Nie udało się połączyć",
                    ex.getMessage())
                    .show();
        }
        setEntityList();
    }

    private void setEntityList() {
            entities.getItems().addAll(session.getClassNames());
            centerContent.setDisable(false);
    }

    @FXML
    public void doSearch() {
        Entity entity = session.getEntity(entities.getValue());
        QueryBuilder pc = new QueryBuilder(session.getCriteriaBuilder(), entity, prepareFieldFilters());
        try {
            List resultList = session.find(pc);
            showResults(resultList, entity);
        } catch (Exception e) {
            Throwable t = e;
            while (t.getCause() != null) {
                t = t.getCause();
            }
            new ErrorDialogCreator(t.getMessage())
                    .show();
        }
    }

    private List<FieldFilter> prepareFieldFilters()  {
        Entity entity = session.getEntity(entities.getValue());
        List<FieldFilter> fieldFilterList = new ArrayList<>();
        ObservableList children = filtersGrid.getChildren();
        for (int i = 0;i < addedFilters.size();++i) {
            FieldProperty fieldProperty = entity.getFieldProperty(((Label)children.get(i * 2)).getText());
            Expression expression = (Expression) ((ChoiceBox)children.get(i * 3 + 1)).getValue();
            String value = ((TextField)children.get(i * 3 + 2)).getText();
            fieldFilterList.add(new FieldFilter(expression, fieldProperty, value));
        }
        return fieldFilterList;
    }

    @FXML
    public void addFilter() {
        if (entities.getValue() == null) {
            return;
        }
        String fieldName = fields.getValue();
        if (addedFilters.contains(fieldName)) {
            return;//Can add twice the same field?
        }
        FieldProperty field = session.getEntity(entities.getValue()).getField(fieldName);
        AbstractComparator comparator = ComparatorFactory.getComparator(field);
        filtersGrid.addRow(filtersGrid.getChildren().size(), comparator.createFilterRow(field));
        addedFilters.add(fieldName);
    }

    private void showResults(List results, Entity entity) {
        ObservableList list = FXCollections.observableList(results);
        TableView tv = new TableView();
        tv.setItems(list);
        tv.prefWidthProperty().bind(rightContent.widthProperty());
        tv.prefHeightProperty().bind(rightContent.heightProperty());
        tv.setRowFactory(v -> {
            TableRow row = new TableRow();
            row.setOnMouseClicked(ev -> cellClicked(ev));
            return row;
        });
        for (FieldProperty fp : entity.getFields()) {
            TableColumn column = new TableColumn(fp.getName());
            column.setCellValueFactory(cellData -> {
                try {
                    Object x = fp.getGetMethod().invoke(((TableColumn.CellDataFeatures) cellData).getValue());
                    return new SimpleObjectProperty<>(x);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
                return null;
            });
            tv.getColumns().add(column);
        }
        rightContent.getChildren().add(tv);
    }

    private void cellClicked(MouseEvent event) {
        Object selectedItem = ((TableRow)event.getSource()).getItem();
        Main.addEntity(selectedItem);
        Stage dialog = new Stage();
        dialog.initModality(Modality.WINDOW_MODAL);//TODO none
        dialog.initOwner(mainPane.getScene().getWindow());
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("/view/entityDetails.fxml"), resources);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        Scene dialogScene = new Scene(root);
        dialog.setTitle(selectedItem.getClass().getSimpleName());
        dialog.setScene(dialogScene);
        dialog.show();
    }
}
