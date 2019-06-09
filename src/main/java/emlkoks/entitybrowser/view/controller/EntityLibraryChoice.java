package emlkoks.entitybrowser.view.controller;

import emlkoks.entitybrowser.Main;
import emlkoks.entitybrowser.connection.ProviderEnum;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by EmlKoks on 30.05.19.
 */
public class EntityLibraryChoice implements Initializable {

    @FXML
    private AnchorPane entityLibraryChoicePane;

    @FXML
    private TextField libraryPath;

    @FXML
    private ChoiceBox<String> providerList;

    @FXML
    private ResourceBundle resources;

    private File entityLibrary;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.resources = resources;
        providerList.getItems().addAll(ProviderEnum.getStringValues());
        providerList.setValue(ProviderEnum.Hibernate.name());
    }

    @FXML
    public void chooseLib() {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter exFilter = new FileChooser.ExtensionFilter(resources.getString("entityLibraryChoice.libFilter"), "*.jar");
        fileChooser.setSelectedExtensionFilter(exFilter);
        entityLibrary = fileChooser.showOpenDialog(entityLibraryChoicePane.getScene().getWindow());
        if(entityLibrary != null)
            libraryPath.setText(entityLibrary.getName());
        else
            libraryPath.setText("");
    }

    @FXML
    public void connect() {
        File file = new File(libraryPath.getText());
        ((Stage) entityLibraryChoicePane.getScene().getWindow()).close();
        Main.getMainController().setEntityList(file, ProviderEnum.valueOf(providerList.getValue()));
    }

    @FXML
    public void cancel() {
        ((Stage) entityLibraryChoicePane.getScene().getWindow()).close();
    }

}
