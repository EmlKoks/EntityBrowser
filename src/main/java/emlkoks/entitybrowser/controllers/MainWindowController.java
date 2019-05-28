package emlkoks.entitybrowser.controllers;

import emlkoks.entitybrowser.Main;
import emlkoks.entitybrowser.QueryCreator;
import emlkoks.entitybrowser.connection.Connection;
import emlkoks.entitybrowser.connection.ProviderEnum;
import emlkoks.entitybrowser.session.Entity;
import emlkoks.entitybrowser.session.FieldProperty;
import emlkoks.entitybrowser.session.Session;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Pair;

import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PersistenceException;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;

/**
 * Created by EmlKoks on 18.03.17.
 */
public class MainWindowController implements Initializable{

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Main.setMainController(this);
        this.resources=resources;
        filtersPane.prefWidthProperty().bind(leftContent.widthProperty());
        filtersPane.prefHeightProperty().bind(leftContent.heightProperty());
        entityList.valueProperty().addListener((observable, oldValue, newValue)->{
            filters.getItems().clear();
            filters.getItems().addAll(session.getEntity(newValue).getFieldsNames());
            filters.setValue(filters.getItems().get(0));
            filterList.getChildren().clear();
            addedFilters.clear();
        });
        debug();
    }

    private void debug(){
        Connection connection = Main.savedConnections.getByName("XX");
        File lib = new File("XX");
        session = new Session(connection, lib, ProviderEnum.EclipseLink);
        if(session.connect()) {
            entityList.getItems().addAll(session.getClassNames());
            centerContent.setDisable(false);
        }
    }

    @FXML
    private void newConnection(){
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/view/newConnection.fxml"), resources);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene dialogScene = new Scene(root);
        dialog.setTitle(resources.getString("newConnection.title"));
        dialog.setScene(dialogScene);
        dialog.show();
    }

    public void createNewSessionTab(Connection connection){
        chooseEntity(connection);
    }

    private void chooseEntity(Connection connection){
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        TextField libPath = new TextField();
        ChoiceBox<String> provider = new ChoiceBox<>();
        provider.getItems().addAll(ProviderEnum.getStringValues());
        provider.setValue(ProviderEnum.Hibernate.name());
        Button chooseLib = new Button("Choose file");
        chooseLib.setOnAction(value -> {
            libPath.setText(chooseEntityLib());
        });
        grid.add(new Label("Library file:"), 0, 0);
        grid.add(libPath, 1, 0);
        grid.add(chooseLib, 2, 0);
        grid.add(new Label("JPA Provider:"), 0, 1);
        grid.add(provider, 1, 1);
        dialog.getDialogPane().setContent(grid);
        ButtonType connectButton = new ButtonType("Connect", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(connectButton, ButtonType.CANCEL);
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == connectButton) {
                File file = new File(libPath.getText());
                session = new Session(connection, file, ProviderEnum.valueOf(provider.getValue()));
                try{
                    session.connect();
                    entityList.getItems().addAll(session.getClassNames());
                    centerContent.setDisable(false);
                } catch (PersistenceException ex) {
                    ex.printStackTrace();
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Nie udało się połączyć");
                    alert.setContentText(ex.getMessage());
                    alert.show();
                }
            }
            return null;
        });

        Optional<Pair<String, String>> result = dialog.showAndWait();
    }

    private String chooseEntityLib(){

        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter exFilter = new FileChooser.ExtensionFilter(resources.getString("choose.lib_filter"), "*.jar", "*.war", "*.earr");
        fileChooser.setSelectedExtensionFilter(exFilter);
        File file = fileChooser.showOpenDialog(mainPane.getScene().getWindow());
        return file.getPath();
    }
    
    @FXML
    public void doSearch() throws NoSuchMethodException {
        Entity entity = session.getEntity(entityList.getValue());
        ObservableList children = filterList.getChildren();
        QueryCreator pc = new QueryCreator(entity.getClazz(), session.getCriteriaBuilder());
        for(int i = 0 ; i<addedFilters.size() ; ++i){
            FieldProperty fp = entity.getFieldProperty(((Label)children.get(i*2)).getText());
            String value = ((TextField)children.get(i*3+2)).getText();
            String expression = ((ChoiceBox)children.get(i*3+1)).getValue().toString();
            pc.createPredicate(fp, expression, value);
        }
        try {
            List resultList = session.find(pc);
            showResults(resultList, entity);
        } catch (Exception e){
            Throwable t = e;
            while(t.getCause() != null)
                t = t.getCause();
            Alert alert = new Alert(Alert.AlertType.ERROR, t.getMessage());
            alert.show();
        }
    }

    @FXML
    public void addFilter(){
        if(entityList.getValue() == null) return;
        String fieldName = filters.getValue();
        if(addedFilters.contains(fieldName)) return;
        Field field = session.getEntity(entityList.getValue()).getField(fieldName);
        Label label = new Label(filters.getValue());
        ChoiceBox<String> expression = new ChoiceBox<>();
        expression.getItems().addAll(getChoiceListByFieldType(field));
        expression.setValue(expression.getItems().get(0));
        TextField value = new TextField();
        filterList.addRow(filterList.getChildren().size(), label, expression, value);
        addedFilters.add(fieldName);
    }

    private void showResults(List results, Entity entity){
        ObservableList list = FXCollections.observableList(results);
        TableView tv = new TableView();
        tv.setItems(list);
        tv.prefWidthProperty().bind(rightContent.widthProperty());
        tv.prefHeightProperty().bind(rightContent.heightProperty());
        tv.setRowFactory(v -> {
            TableRow row = new TableRow();
            row.setOnMouseClicked( ev -> cellClicked(ev));
            return row;
        });
        for(FieldProperty fp : entity.getFields()){
            TableColumn column = new TableColumn(fp.getName());
            column.setCellValueFactory(cellData -> {
                try {
                    Object x = fp.getGetMethod().invoke(((TableColumn.CellDataFeatures) cellData).getValue());
                    return new SimpleObjectProperty<>(x);
                } catch (IllegalAccessException|InvocationTargetException e) {
                    e.printStackTrace();
                }
                return null;
            });
            tv.getColumns().add(column);
        }
        rightContent.getChildren().add(tv);
    }



    private String[] getChoiceListByFieldType(Field field) {
        Class clazz = field.getType();
        List<String> choices = new ArrayList<>();
        if (clazz == String.class) {
            choices.add("Równe");
            choices.add("Zawiera");
        } else if(clazz == boolean.class || clazz == Boolean.class) {
            choices.add("Równe");
        } else if (clazz.isAssignableFrom(Number.class) || clazz == int.class || clazz == float.class || clazz == long.class || clazz == double.class) {
            choices.add("==");
            choices.add("=!");
            choices.add(">");
            choices.add(">=");
            choices.add("<");
        } else if (clazz == Date.class) {
            choices.add("==");
            choices.add(">");
            choices.add(">=");
            choices.add("<");
        } else if (field.getAnnotation(OneToMany.class) != null){
            choices.add("OneToMany");
        } else if (field.getAnnotation(ManyToOne.class) != null){
            choices.add("ManyToOne");
        } else {
            System.out.println("Nieobsługiwany typ danych " + clazz);
        }
        return choices.toArray(new String[choices.size()]);
    }
    
    private void cellClicked(MouseEvent event){
        Object selectedItem = ((TableRow)event.getSource()).getItem();
        Main.addEntity(selectedItem);
        Stage dialog = new Stage();
        dialog.initModality(Modality.WINDOW_MODAL);
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
