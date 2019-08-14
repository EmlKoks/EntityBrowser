package emlkoks.entitybrowser.view.controller;

import emlkoks.entitybrowser.connection.Connection;
import emlkoks.entitybrowser.connection.Provider;
import emlkoks.entitybrowser.session.Session;
import emlkoks.entitybrowser.view.dialog.ErrorDialogCreator;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Created by EmlKoks on 30.05.19.
 */
public class NewSessionController implements Initializable {

    @FXML
    private AnchorPane newSessionPane;

    @FXML
    private TextField libraryPath;

    @FXML
    private TextField connectionName;

    @FXML
    private ChoiceBox<String> providerList;

    @FXML
    private ResourceBundle resources;

    private File entityLibrary;

    private Connection connection;

    private Session session;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.resources = resources;
        providerList.getItems().addAll(Provider.getStringValues());
        providerList.setValue(Provider.Hibernate.name());
    }

    @FXML
    public void chooseConnection() {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/newConnection.fxml"), resources);
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene dialogScene = new Scene(loader.getRoot());
        stage.setTitle(resources.getString("newConnection.title"));
        stage.setScene(dialogScene);
        stage.showAndWait();
        NewConnectionController controller = loader.getController();
        connection = controller.getConnection();
        if (connection != null) {
            connectionName.setText(connection.getName());
        }
    }

    @FXML
    public void chooseLibrary() {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter exFilter = new FileChooser.ExtensionFilter(
                resources.getString("newSession.libFilter"), "*.jar", "*.war", "*.ear");
        fileChooser.setSelectedExtensionFilter(exFilter);
        File selectedFile = fileChooser.showOpenDialog(newSessionPane.getScene().getWindow());
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
            createSession();
            ((Stage) newSessionPane.getScene().getWindow()).close();
        }
    }

    @FXML
    public void cancel() {
        ((Stage) newSessionPane.getScene().getWindow()).close();
    }


    private void createSession() {
        try {
            session = new Session(connection, entityLibrary, getProvider());
            session.connect();
        } catch (Exception ex) {
            ex.printStackTrace();

            new ErrorDialogCreator(
                    "Nie udało się połączyć",
                    ex.getMessage())
                    .show();
        }
    }

    private Provider getProvider() {
        return Provider.valueOf(providerList.getValue());
    }

    Session getSession() {
        return session;
    }

}
