package emlkoks.entitybrowser.view.controller;

import emlkoks.entitybrowser.Main;
import emlkoks.entitybrowser.connection.Connection;
import emlkoks.entitybrowser.connection.Provider;
import emlkoks.entitybrowser.query.QueryCreator;
import emlkoks.entitybrowser.query.comparator.ComparatorManager;
import emlkoks.entitybrowser.query.comparator.ComparatorNotFoundException;
import emlkoks.entitybrowser.query.comparator.expression.Expression;
import emlkoks.entitybrowser.session.Entity;
import emlkoks.entitybrowser.session.FieldProperty;
import emlkoks.entitybrowser.session.Session;
import emlkoks.entitybrowser.view.dialog.ErrorDialogCreator;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
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
    private ChoiceBox<String> entityList;

    @FXML
    private ChoiceBox<String> filters;

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
    private GridPane filterList;

    private Session session;

    private Set<String> addedFilters = new HashSet<>();

    private Connection connection;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Main.setMainController(this);
        this.resources = resources;
        filtersPane.prefWidthProperty().bind(leftContent.widthProperty());
        filtersPane.prefHeightProperty().bind(leftContent.heightProperty());
        entityList.valueProperty().addListener((observable, oldValue, newValue) -> {
            filters.getItems().clear();
            filters.getItems().addAll(session.getEntity(newValue).getFieldsNames());
            filters.setValue(filters.getItems().get(0));
            filterList.getChildren().clear();
            addedFilters.clear();
        });
        debug();
    }

    private void debug() {
        Connection connection = Main.savedConnections.getByName("ers");
        File lib = new File("/home/koks/Projekty/EntityBrowser/ERS-db-entities-1.1.jar");
        session = new Session(connection, lib, Provider.EclipseLink);
        if (session.connect()) {
            entityList.getItems().addAll(session.getClassNames());
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
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/view/newConnection.fxml"), resources);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene dialogScene = new Scene(root);
        stage.setTitle(resources.getString("newConnection.title"));
        stage.setScene(dialogScene);
        stage.show();
    }

    void createNewSessionTab(Connection connection) {
        this.connection = connection;
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/entityLibraryChoice.fxml"), resources);
            Scene dialogScene = new Scene(root);
            stage.setTitle(resources.getString("entityLibraryChoice.title"));
            stage.setScene(dialogScene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void setEntityList(File file, Provider provider) {
        session = new Session(connection, file, provider);
        try {
            session.connect();
            entityList.getItems().addAll(session.getClassNames());
            centerContent.setDisable(false);
        } catch (PersistenceException ex) {
            ex.printStackTrace();

            new ErrorDialogCreator(
                    "Nie udało się połączyć",
                    ex.getMessage())
                    .show();
        }
    }

    @FXML
    public void doSearch() {
        Entity entity = session.getEntity(entityList.getValue());
        ObservableList children = filterList.getChildren();
        QueryCreator pc = new QueryCreator(entity.getClazz(), session.getCriteriaBuilder());
        for (int i = 0;i < addedFilters.size();++i) {
            FieldProperty fp = entity.getFieldProperty(((Label)children.get(i * 2)).getText());
            String value = ((TextField)children.get(i * 3 + 2)).getText();
            String expression = ((ChoiceBox)children.get(i * 3 + 1)).getValue().toString();
            pc.createPredicate(fp, expression, value);
        }
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

    @FXML
    public void addFilter() {
        if (entityList.getValue() == null) {
            return;
        }
        String fieldName = filters.getValue();
        if (addedFilters.contains(fieldName)) {
            return;
        }
        Field field = session.getEntity(entityList.getValue()).getField(fieldName);
        Label label = new Label(filters.getValue());
        ChoiceBox<Expression> expression = new ChoiceBox<>();
        try {
            expression.getItems().addAll(ComparatorManager.getExpressionByField(field));
        } catch (ComparatorNotFoundException ex) {
            log.info(ex.getMessage());
            //TODO show dialog
            return;
        }
        expression.setValue(expression.getItems().get(0));
        TextField value = new TextField();
        filterList.addRow(filterList.getChildren().size(), label, expression, value);
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
