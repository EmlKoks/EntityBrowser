package emlkoks.entitybrowser.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by EmlKoks on 28.03.17.
 */
public class NewDriverController implements Initializable{

    @FXML
    GridPane newDriverDialog;

    @FXML
    TextField driver;

    @FXML
    TextField name;

    @FXML
    TextField clazz;

    @FXML
    TextField urlTemplate;

    @FXML
    private ResourceBundle resources;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.resources = resources;
    }

    @FXML
    public void chooseLib() {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter exFilter = new FileChooser.ExtensionFilter(resources.getString("choose.lib_filter"), "*.jar");
        fileChooser.setSelectedExtensionFilter(exFilter);
        File file = fileChooser.showOpenDialog(newDriverDialog.getScene().getWindow());
        if(file != null)
            driver.setText(file.getName());
    }

    @FXML
    public void closeDialog(){
        ((Stage)newDriverDialog.getScene().getWindow()).close();
    }

    @FXML
    public void save(){

    }
}
