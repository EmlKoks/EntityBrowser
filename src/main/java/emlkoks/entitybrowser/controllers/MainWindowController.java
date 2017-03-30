package emlkoks.entitybrowser.controllers;

import emlkoks.entitybrowser.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.persistence.EntityManagerFactory;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by EmlKoks on 18.03.17.
 */
public class MainWindowController implements Initializable{

    @FXML
    private Button bChooseLib;

    @FXML
    private TextField tChoosedLib;

    @FXML
    private ResourceBundle resources;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Main.setMainController(this);
        this.resources=resources;
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("/mnt/dysk/Programowanie/koks312-adressbook-a508f653c32e/model/target"));
        FileChooser.ExtensionFilter exFilter = new FileChooser.ExtensionFilter(resources.getString("choose.lib_filter"), "*.jar");
        fileChooser.setSelectedExtensionFilter(exFilter);
        bChooseLib.setOnAction((x)->{
            File file = fileChooser.showOpenDialog(bChooseLib.getContextMenu());
            tChoosedLib.setText(file.getName());
        });
    }

    @FXML
    private void newConnection(){
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(bChooseLib.getContextMenu());
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

    public void addTab(EntityManagerFactory emf){

    }
}
