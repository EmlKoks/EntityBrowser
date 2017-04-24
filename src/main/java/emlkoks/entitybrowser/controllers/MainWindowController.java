package emlkoks.entitybrowser.controllers;

import emlkoks.entitybrowser.Main;
import emlkoks.entitybrowser.QueryCreator;
import emlkoks.entitybrowser.connection.Connection;
import emlkoks.entitybrowser.session.Entity;
import emlkoks.entitybrowser.session.FieldProperty;
import emlkoks.entitybrowser.session.Session;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.persistence.OneToMany;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
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
        File lib = chooseEntityLib();
        session = new Session(connection, lib);
        if(session.connect()) {
            entityList.getItems().addAll(session.getClassNames());
            centerContent.setDisable(false);
        }
    }

    private File chooseEntityLib(){
        FileChooser fileChooser = new FileChooser();
//        fileChooser.setInitialDirectory(new File("/mnt/dysk/Programowanie/koks312-adressbook-a508f653c32e/model/target"));
        fileChooser.setInitialDirectory(new File("/mnt/dysk/Programowanie/XXX/target"));
        FileChooser.ExtensionFilter exFilter = new FileChooser.ExtensionFilter(resources.getString("choose.lib_filter"), "*.jar", "*.war");
        fileChooser.setSelectedExtensionFilter(exFilter);
        File file = fileChooser.showOpenDialog(mainPane.getScene().getWindow());
        return file;
    }
    
    @FXML
    public void doFilter() throws NoSuchMethodException {
        Entity entity = session.getEntity(entityList.getValue());
        ObservableList children = filterList.getChildren();
        QueryCreator pc = new QueryCreator(entity.getClazz(), session.getCriteriaBuilder());
        for(int i = 0 ; i<addedFilters.size() ; ++i){
            FieldProperty fp = entity.getFieldProperty(((Label)children.get(i*2)).getText());
            String value = ((TextField)children.get(i*3+2)).getText();
            pc.createPredicate(fp, "", value);
        }
        List resultList = session.find(pc);
        showResults(resultList, entity);
    }

    @FXML
    public void addFilter(){
        String fieldName = filters.getValue();
        if(addedFilters.contains(fieldName)) return;
        Field field = session.getEntity(entityList.getValue()).getField(fieldName);
        Label label = new Label(filters.getValue());
        ChoiceBox<String> expression = new ChoiceBox<>();
        expression.getItems().addAll(getChoiceListByFieldType(field.getType()));
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



    private String[] getChoiceListByFieldType(Class clazz) {
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
        } else if (clazz.getAnnotation(OneToMany.class) != null){
            choices.add("Zawiera");
        } else {
            System.out.println("Nieobsługiwany typ danych " + clazz);
        }
        return choices.toArray(new String[choices.size()]);
    }
    
    private void cellClicked(MouseEvent event){
        Object selectedItem = ((TableRow)event.getSource()).getItem();
    }
    
    
}
