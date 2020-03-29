package emlkoks.entitybrowser.view.controller;

import emlkoks.entitybrowser.connection.Connection;
import emlkoks.entitybrowser.connection.Property;
import emlkoks.entitybrowser.connection.Provider;
import emlkoks.entitybrowser.connection.provider.HibernateProvider;
import emlkoks.entitybrowser.session.Session;
import emlkoks.entitybrowser.view.dialog.ErrorDialogCreator;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;
import java.util.TooManyListenersException;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.converter.DefaultStringConverter;
import javafx.util.converter.IntegerStringConverter;

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

    @FXML
    private ToggleButton toggleAdvanced;

    @FXML
    private SplitPane splitPane;

    @FXML
    private ScrollPane advancedPane;

    @FXML
    private TableView<Property> properties;
    @FXML
    private TableColumn<Property, String> nameColumn;
    @FXML
    private TableColumn<Property, String> valueColumn;

    private File entityLibrary;

    private Connection connection;

    private Session session;

    private ObservableList<Property> props;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.resources = resources;
        providerList.getItems().addAll(Provider.getStringValues());
        providerList.setValue(Provider.Hibernate.name());
        initializePropertiesTable();
    }

    private void initializePropertiesTable() {
        props = FXCollections.observableList(
                new ArrayList<>(new HibernateProvider().getDefaultProperties()));
        properties.setItems(props);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DefaultStringConverter()));
        valueColumn.setCellValueFactory(new PropertyValueFactory<>("value"));
//        hideAdvancedPane();
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

    @FXML
    public void toggleAdvanced() {
        if (toggleAdvanced.isSelected()) {
            showAdvancedPane();
        } else {
            hideAdvancedPane();
        }
    }

    private void showAdvancedPane() {
        splitPane.getItems().add(1, advancedPane);
    }

    private void hideAdvancedPane() {
        splitPane.getItems().remove(advancedPane);
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

    public Session getSession() {
        return session;
    }

}
