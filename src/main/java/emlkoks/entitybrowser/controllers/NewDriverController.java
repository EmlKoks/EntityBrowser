package emlkoks.entitybrowser.controllers;

import emlkoks.entitybrowser.Main;
import emlkoks.entitybrowser.Util;
import emlkoks.entitybrowser.connection.Driver;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
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
    private GridPane newDriverDialog;

    @FXML
    private TextField driver;

    @FXML
    private TextField name;

    @FXML
    private TextField clazz;

    @FXML
    private TextField urlTemplate;

    private File lib;

    @FXML
    private ResourceBundle resources;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.resources = resources;
    }

    @FXML
    public void chooseLib() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("/mnt/dysk/Programowanie/koks312-adressbook-a508f653c32e/model/target"));
        FileChooser.ExtensionFilter exFilter = new FileChooser.ExtensionFilter(resources.getString("choose.lib_filter"), "*.jar");
        fileChooser.setSelectedExtensionFilter(exFilter);
        lib = fileChooser.showOpenDialog(newDriverDialog.getScene().getWindow());
        if(lib != null)
            driver.setText(lib.getName());
        else
            driver.setText("");
    }

    @FXML
    public void closeDialog(){
        ((Stage)newDriverDialog.getScene().getWindow()).close();
    }

    @FXML
    public void save(){
        if(lib == null ||
                Util.isNullOrEmpty(name.getText()) ||
                Util.isNullOrEmpty(clazz.getText()) ||
                Util.isNullOrEmpty(urlTemplate.getText())){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(resources.getString("newDriver.error.title"));
            alert.setContentText(resources.getString("newDriver.error.content"));
            alert.show();
        } else {
            Driver driver = new Driver();
            driver.setName(name.getText());
            driver.setClassName(clazz.getText());
            driver.setUrl(urlTemplate.getText());
            System.out.println("lib.getPath() = " + lib.getPath());
            System.out.println("lib.getAbsolutePath() = " + lib.getAbsolutePath());
            driver.setLib(lib.getPath()); //todo copy do local dir
            Main.drivers.add(driver); //todo test driver
            closeDialog();
        }

    }
}
