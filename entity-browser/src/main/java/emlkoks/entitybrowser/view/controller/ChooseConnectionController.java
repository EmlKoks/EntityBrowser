package emlkoks.entitybrowser.view.controller;

import com.google.common.base.Strings;
import emlkoks.entitybrowser.Main;
import emlkoks.entitybrowser.Mode;
import emlkoks.entitybrowser.connection.Connection;
import emlkoks.entitybrowser.connection.Driver;
import emlkoks.entitybrowser.connection.Property;
import emlkoks.entitybrowser.connection.Provider;
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
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.BorderPane;
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
    @FXML private TabPane tabPane;
    @FXML private ResourceBundle resources;
    @FXML private ListView<Connection> savedConnections;
    @FXML private TextField connectionNameField;
    @FXML private ChoiceBox<String> driversChoiceBox;
    @FXML private TextField urlField;
    @FXML private TextField userField;
    @FXML private TextField passwordField;
    @FXML private TextField libraryPathField;
    @FXML private ChoiceBox<Provider> providersChoiceBox;
    @FXML private TableView<Property> propertiesTable;
    @FXML private TableColumn<Property, String> nameColumn;
    @FXML private TableColumn<Property, String> valueColumn;

    private Connection connection;
    private Session session;


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
        if (Mode.DEBUG.equals(Main.mode)) {
            savedConnections.getSelectionModel().select(2);
            tabPane.getSelectionModel().select(1);
        }
    }

    private void fillSavedConnections() {
        savedConnections.setItems(Main.savedConnections.getConnections());
        savedConnections.setCellFactory(view -> new ConnectionListCell());
    }

    private void initDriverList() {
        driversChoiceBox.getItems().addAll(Main.drivers.getDriverNames());
        driversChoiceBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            Driver driver = Main.drivers.getDriver(newValue);
            if (Objects.nonNull(driver)) {
                urlField.setText(driver.getUrlTemplate());
            }
        });
    }

    private void initProviders() {
        providersChoiceBox.getItems().addAll(Provider.values());
        providersChoiceBox.setValue(Provider.DEFAULT);
    }

    private void initPropertiesTable() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DefaultStringConverter()));
        nameColumn.setOnEditCommit(event -> event.getRowValue().setName(event.getNewValue()));
        valueColumn.setCellValueFactory(new PropertyValueFactory<>("value"));
        valueColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DefaultStringConverter()));
        valueColumn.setOnEditCommit(event -> event.getRowValue().setValue(event.getNewValue()));
    }

    @FXML
    public void addNewConnection() {
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
        propertiesTable.setItems(connection.getProperties());
    }

    @FXML
    public void deleteConnection() {
        Optional<ButtonType> result =
                new ConfirmationDialogCreator(
                        resources.getString("error.title"),
                        resources.getString("newDriver.removeConnection") + connection.getName() + "?")
                        .showAndWait();
        if (result.orElse(ButtonType.CANCEL).equals(ButtonType.OK)) {
            Main.savedConnections.remove(connection.getId());
        }
    }

    @FXML
    public void chooseLibrary() {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter exFilter = new FileChooser.ExtensionFilter(
                resources.getString("chooseConnection.libFilter"), "*.jar", "*.war", "*.ear");
        fileChooser.setSelectedExtensionFilter(exFilter);
        File selectedFile = fileChooser.showOpenDialog(mainPane.getScene().getWindow());
        if (selectedFile != null) {
            libraryPathField.setText(selectedFile.getAbsolutePath());
        } else {
            libraryPathField.setText("");
        }
    }

    @FXML
    public void connect() {
        fillConnection();
        if (createSession()) {
            ((Stage) mainPane.getScene().getWindow()).close();
        }
    }

    @FXML
    public void cancel() {
        ((Stage) mainPane.getScene().getWindow()).close();
    }

    private boolean createSession() {
        try {
            session = new Session(connection);
            session.connect();
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();

            new ErrorDialogCreator(
                    "Nie udało się połączyć",
                    ex.getMessage())
                    .show();
        }
        return false;
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
        fillConnection();//TODO add observable
        if (Strings.isNullOrEmpty(connection.getName())) {
            new WarningDialogCreator(
                    resources.getString("error.title"),
                    resources.getString("chooseConnection.emptyName"))
                    .show();
        } else {
            Main.savedConnections.saveConnection(connection);
        }
    }

    @FXML
    public void testConnection() {
        fillConnection();
        if (!validateConnection()) {
            new WarningDialogCreator(
                    resources.getString("error.title"),
                    resources.getString("chooseConnection.test.wrong.emptyFields"))
                    .show();
        } else if (connection.connectionTest()) {
            //TODO show dialog with exception details
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

    @FXML
    public void addRow() {
        connection.getProperties().add(new Property());
    }

    @FXML
    public void deleteRow() {
        connection.getProperties().removeAll(propertiesTable.getSelectionModel().getSelectedItems());
    }

    @FXML
    public void copyRow() {

    }

    private void fillConnection() {
        if (Objects.isNull(connection)) {
            connection = new Connection();
        }
        connection.setName(connectionNameField.getText());
        connection.setDriver(Main.drivers.getDriver(driversChoiceBox.getValue()));
        connection.setUrl(urlField.getText());
        connection.setUser(userField.getText());
        connection.setPassword(passwordField.getText());
        connection.setLibraryPath(libraryPathField.getText());
        connection.setProvider(providersChoiceBox.getValue());
    }

    private boolean validateConnection() {
        return !Strings.isNullOrEmpty(driversChoiceBox.getValue())
                && !Strings.isNullOrEmpty(urlField.getText())
                && !Strings.isNullOrEmpty(userField.getText())
                && !Strings.isNullOrEmpty(passwordField.getText());
    }
}
