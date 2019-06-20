package emlkoks.entitybrowser.view.controller;

import emlkoks.entitybrowser.connection.Provider;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * Created by EmlKoks on 30.05.19.
 */
public class EntityLibraryChoiceController implements Initializable {

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
        providerList.getItems().addAll(Provider.getStringValues());
        providerList.setValue(Provider.Hibernate.name());
    }

    @FXML
    public void chooseLib() {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter exFilter = new FileChooser.ExtensionFilter(
                resources.getString("entityLibraryChoice.libFilter"), "*.jar", "*.war", "*.ear");
        fileChooser.setSelectedExtensionFilter(exFilter);
        File selectedFile = fileChooser.showOpenDialog(entityLibraryChoicePane.getScene().getWindow());
        if (selectedFile != null) {
            libraryPath.setText(selectedFile.getName());
        } else {
            libraryPath.setText("");
        }
    }

    @FXML
    public void connect() {
        entityLibrary = new File(libraryPath.getText());
        if (entityLibrary.exists()) {
            ((Stage) entityLibraryChoicePane.getScene().getWindow()).close();
        }
    }

    @FXML
    public void cancel() {
        ((Stage) entityLibraryChoicePane.getScene().getWindow()).close();
    }

    Provider getProvider() {
        return Provider.valueOf(providerList.getValue());
    }

    File getEntityLibrary() {
        return entityLibrary;
    }

}
