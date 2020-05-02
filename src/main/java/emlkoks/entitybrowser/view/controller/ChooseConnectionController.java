package emlkoks.entitybrowser.view.controller;

import emlkoks.entitybrowser.Main;
import emlkoks.entitybrowser.common.Util;
import emlkoks.entitybrowser.connection.Connection;
import emlkoks.entitybrowser.connection.Driver;
import emlkoks.entitybrowser.connection.Property;
import emlkoks.entitybrowser.connection.Provider;
import emlkoks.entitybrowser.connection.provider.HibernateProvider;
import emlkoks.entitybrowser.resources.Resources;
import emlkoks.entitybrowser.session.Session;
import emlkoks.entitybrowser.view.ViewFile;
import emlkoks.entitybrowser.view.control.ConnectionListCell;
import emlkoks.entitybrowser.view.dialog.ConfirmationDialogCreator;
import emlkoks.entitybrowser.view.dialog.ErrorDialogCreator;
import emlkoks.entitybrowser.view.dialog.InformationDialogCreator;
import emlkoks.entitybrowser.view.dialog.WarningDialogCreator;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.converter.DefaultStringConverter;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by EmlKoks on 30.05.19.
 */
@Slf4j
public class ChooseConnectionController implements Initializable {

    @FXML private BorderPane mainPane;
    @FXML private ResourceBundle resources;
    @FXML private ListView<Connection> savedConnections;
    @FXML private TextField connectionNameField;
    @FXML private ChoiceBox<String> driversChoiceBox;
    @FXML private TextField urlField;
    @FXML private TextField userField;
    @FXML private TextField passwordField;
    @FXML private TextField libraryPathField;
    @FXML private ChoiceBox<Provider> providersChoiceBox;
    @FXML private VBox pane;
    @FXML private ScrollPane advancedPane;
    @FXML private TableView<Property> propertiesTable;
    @FXML private TableColumn<Property, String> nameColumn;
    @FXML private TableColumn<Property, String> valueColumn;

    private Connection connection;
    private File entityLibrary;
    private Session session;
    private ObservableList<Property> props;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.resources = resources;
        initSavedConnections();
        initDriverList();
        initProviders();
        initPropertiesTable();
    }

    private void initSavedConnections() {
        fillSavedConnections();
        savedConnections.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            connection = newValue;
            fillView();
        });
    }

    private void fillSavedConnections() {
        savedConnections.setItems(Main.savedConnections.getList());
        savedConnections.setCellFactory(view -> new ConnectionListCell());
    }

    private void initDriverList() {
        driversChoiceBox.getItems().addAll(Main.drivers.getDriverNames());
        driversChoiceBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            Driver driver = Main.drivers.getDriver(newValue);
            if (Objects.nonNull(driver)) {
                urlField.setText(driver.getUrl());
            }
        });
    }

    private void initProviders() {
        providersChoiceBox.getItems().addAll(Provider.values());
        providersChoiceBox.setValue(Util.DEFAULT_PROVIDER);
    }

    private void initPropertiesTable() {
        props = FXCollections.observableList(
                new ArrayList<>(new HibernateProvider().getDefaultProperties()));
        propertiesTable.setItems(props);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DefaultStringConverter()));
        valueColumn.setCellValueFactory(new PropertyValueFactory<>("value"));
    }

    @FXML
    private void addNewConnection() {
        connection = Main.savedConnections.newConnection();
        fillView();
    }

    private void fillView() {
        connectionNameField.setText(connection.getName());
        driversChoiceBox.setValue(Objects.nonNull(connection.getDriver()) ? connection.getDriver().getName() : null);
        urlField.setText(connection.getUrl());
        userField.setText(connection.getUser());
        passwordField.setText(connection.getPassword());
        libraryPathField.setText(connection.getLibraryPath());
        providersChoiceBox.setValue(connection.getProvider());
    }

    @FXML
    private void deleteConnection() {
        Optional<ButtonType> result =
                new ConfirmationDialogCreator(
                        resources.getString("error.title"),
                        resources.getString("newDriver.removeConnection") + connection.getName() + "?")
                        .showAndWait();
        if (result.get() == ButtonType.OK) {
            Main.savedConnections.remove(connection.getId());
        }
    }

    @FXML
    public void chooseLibrary() {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter exFilter = new FileChooser.ExtensionFilter(
                resources.getString("newSession.libFilter"), "*.jar", "*.war", "*.ear");
        fileChooser.setSelectedExtensionFilter(exFilter);
        File selectedFile = fileChooser.showOpenDialog(mainPane.getScene().getWindow());
        if (selectedFile != null) {
            libraryPathField.setText(selectedFile.getName());
        } else {
            libraryPathField.setText("");
        }
    }

    @FXML
    public void connect() {
        entityLibrary = new File(libraryPathField.getText());
        if (entityLibrary.exists()) {
            createSession();
            ((Stage) mainPane.getScene().getWindow()).close();
        }
    }

    @FXML
    public void cancel() {
        ((Stage) mainPane.getScene().getWindow()).close();
    }

    private void createSession() {
        try {
            //TODO bind properties to connection
            session = new Session(new Connection(), entityLibrary, providersChoiceBox.getValue());
            session.connect();
        } catch (Exception ex) {
            ex.printStackTrace();

            new ErrorDialogCreator(
                    "Nie udało się połączyć",
                    ex.getMessage())
                    .show();
        }
    }

    public Session getSession() {
        return session;
    }

    @FXML
    public void addDriver() {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(mainPane.getScene().getWindow());
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource(ViewFile.NEW_DRIVER.getFile()), resources);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        Scene dialogScene = new Scene(root);
        dialog.setTitle(resources.getString("newDriver.title"));
        dialog.setScene(dialogScene);
        dialog.showAndWait();
        driversChoiceBox.getItems().clear();
        driversChoiceBox.getItems().addAll(Main.drivers.getDriverNames());
    }

    @FXML
    public void saveConnection() {
        fillConnection();
        Main.savedConnections.saveConnection(connection);
    }

    @FXML
    public void testConnection() {
        if (!checkFields()) {
            //TODO nothing?
        } else if (createConnection() == null) {
            new WarningDialogCreator(
                    resources.getString("chooseConnection.test.title"),
                    resources.getString("chooseConnection.test.wrong.content"))
                    .show();
        } else {
            new InformationDialogCreator(
                    resources.getString("chooseConnection.test.title"),
                    resources.getString("chooseConnection.test.correct.content"))
                    .show();
        }
    }

    private void fillConnection() {
        connection.setName(connectionNameField.getText());
        connection.setDriver(Main.drivers.getDriver(driversChoiceBox.getValue()));
        connection.setUrl(urlField.getText());
        connection.setUser(userField.getText());
        connection.setPassword(passwordField.getText());
        connection.setLibraryPath(libraryPathField.getText());
        connection.setProvider(providersChoiceBox.getValue());
    }

    private Connection createConnection() {
//        Connection connection = new Connection();
//        connection.setName("Custom");//TODO use saved connection name
//        connection.setDriver(Main.drivers.getDriver(drivers.getValue()));
//        connection.setUrl(url.getText());
//        connection.setUser(user.getText());
//        connection.setPassword(password.getText());
//        if (Connector.testConnection(connection)) {
//            return connection;
//        } else {
            return null;
//        }
    }

    private boolean checkFields() {
        if (Resources.isNullOrEmpty(driversChoiceBox.getValue())
                || Resources.isNullOrEmpty(urlField.getText())
                || Resources.isNullOrEmpty(userField.getText())
                || Resources.isNullOrEmpty(passwordField.getText())) {
            new ErrorDialogCreator(
                    resources.getString("error.title"),
                    resources.getString("newDriver.error.content"))
                    .show();
            return false;
        }
        return true;
    }
}
