package emlkoks.entitybrowser.view.controller;

import emlkoks.entitybrowser.Main;
import emlkoks.entitybrowser.connection.Driver;
import emlkoks.entitybrowser.common.Resources;
import emlkoks.entitybrowser.view.dialog.ErrorDialogCreator;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.commons.io.FileUtils;


/**
 * Created by EmlKoks on 28.03.17.
 */
public class NewDriverController implements Initializable {

    @FXML private GridPane newDriverPane;
    @FXML private TextField driver;
    @FXML private TextField name;
    @FXML private TextField clazz;
    @FXML private TextField urlTemplate;
    @FXML private ResourceBundle resources;

    private File lib;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.resources = resources;
    }

    @FXML
    public void chooseLib() {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter exFilter = new FileChooser.ExtensionFilter(
                resources.getString("newDriver.libFilter"), "*.jar");
        fileChooser.setSelectedExtensionFilter(exFilter);
        lib = fileChooser.showOpenDialog(newDriverPane.getScene().getWindow());
        if (lib != null) {
            driver.setText(lib.getName());
        } else {
            driver.setText("");
        }
    }

    @FXML
    public void closeDialog() {
        ((Stage) newDriverPane.getScene().getWindow()).close();
    }

    @FXML
    public void save() {
        if (lib == null
                || Resources.isNullOrEmpty(name.getText())
                || Resources.isNullOrEmpty(clazz.getText())
                || Resources.isNullOrEmpty(urlTemplate.getText())) {
            new ErrorDialogCreator(
                    resources.getString("error.title"),
                    resources.getString("newDriver.error.emptyFields"))
                    .show();
        } else {
            if (createDriver()) {
                closeDialog();
            } else {
                new ErrorDialogCreator(
                        resources.getString("error.title"),
                        resources.getString("newDriver.error.copylibToLocalDir"))
                        .show();
            }
        }

    }

    private boolean createDriver() {
        Driver driver = new Driver();
        driver.setName(name.getText());
        driver.setClassName(clazz.getText());
        driver.setUrl(urlTemplate.getText());
        File destLibFile = new File(Resources.DRIVERS_DIR, lib.getName());
        try {
            FileUtils.copyFile(lib, destLibFile);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        driver.setLib(lib.getName());
        Main.drivers.add(driver);
        return true;
    }
}
