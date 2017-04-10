package emlkoks.entitybrowser.controllers;

import emlkoks.entitybrowser.Main;
import emlkoks.entitybrowser.connection.Connection;
import emlkoks.entitybrowser.session.Session;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

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
    private VBox filterList;

    private Session session;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Main.setMainController(this);
        this.resources=resources;
        entityList.valueProperty().addListener((observable, oldValue, newValue)->{
            filters.getItems().clear();
            filters.getItems().addAll(session.getEntity(newValue).getFields().keySet());
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
        fileChooser.setInitialDirectory(new File("/mnt/dysk/Programowanie/koks312-adressbook-a508f653c32e/model/target"));
        FileChooser.ExtensionFilter exFilter = new FileChooser.ExtensionFilter(resources.getString("choose.lib_filter"), "*.jar");
        fileChooser.setSelectedExtensionFilter(exFilter);
        File file = fileChooser.showOpenDialog(mainPane.getScene().getWindow());
        return file;
    }
    
    @FXML
    public void doFilter(){
        List list = session.find(entityList.getValue());
        System.out.println("list.toString() = " + list.toString());
    }
}
