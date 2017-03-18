package emlkoks.entitybrowser.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by EmlKoks on 18.03.17.
 */
public class ChooseEntities implements Initializable{

    @FXML
    private Button bChooseLib;

    @FXML
    private TextField tChoosedLib;

    @FXML
    private ResourceBundle resources;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
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
}
