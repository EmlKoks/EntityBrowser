package emlkoks.entitybrowser.controllers;

import emlkoks.entitybrowser.Main;
import emlkoks.entitybrowser.Util;
import emlkoks.entitybrowser.connection.Connector;
import emlkoks.entitybrowser.connection.Driver;
import emlkoks.entitybrowser.connection.Connection;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Created by EmlKoks on 18.03.17.
 */
public class NewConnectionController implements Initializable{

    @FXML
    private ResourceBundle resources;

    @FXML
    private BorderPane newConnectionDialog;

    @FXML
    private ListView<String> savedConnection;

    @FXML
    private ChoiceBox<String> driverList;

    @FXML
    private TextField url;

    @FXML
    private TextField user;

    @FXML
    private TextField password;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.resources = resources;
        setSavedConnection();
        driverList.getItems().addAll(Main.drivers.getDriverNames());
        driverList.valueProperty().addListener((observable, oldValue, newValue) -> {
            Driver driver = Main.drivers.getDriver(newValue);
            url.setText(driver.getUrl());
        });

        savedConnection.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            Connection connection = Main.savedConnections.getConnection(newValue);
            if(connection == null) return;
            driverList.setValue(connection.getDriver().getName());
            url.setText(connection.getUrl());
            user.setText(connection.getUser());
            password.setText(connection.getPassword());
        });
    }

    @FXML
    private void deleteConnection(){
        List<String> selectedItems = savedConnection.getSelectionModel().getSelectedItems();
        for(String item : selectedItems) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Uwaga!");
            alert.setContentText("Czy na pewno chcesz usunąć połączenie " + item + "?");
            Optional<ButtonType> result = alert.showAndWait();
            if(result.get() == ButtonType.OK){
                Main.savedConnections.remove(item);
                savedConnection.getItems().remove(item);
            }
        }
    }

    private void setSavedConnection(){
        for(Connection sc : Main.savedConnections.getList())
            savedConnection.getItems().add(sc.getName());
    }

    @FXML
    public void saveConnection(){
        while(true){
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle(resources.getString("newConnection.save.title"));
            dialog.setContentText(resources.getString("newConnection.save.content"));
            Optional<String> result = dialog.showAndWait();
            if(!result.isPresent()) return;
            if(Util.isNullOrEmpty(result.get())) continue;
            boolean exist = false;
            for(Connection sc : Main.savedConnections.getList())
                if(sc.getName().equals(result.get())){
                    exist=true;
                    break;
                }
            if (!exist){
                Connection newConnection = new Connection();
                newConnection.setName(result.get());
                newConnection.setUrl(url.getText());
                newConnection.setUser(user.getText());
                newConnection.setPassword(password.getText());
                newConnection.setDriver(Main.drivers.getDriver(driverList.getValue()));
                addNewConnection(newConnection);
                return;
            }
        }
    }

    @FXML
    public void connect(){
        Connection connection = checkConnection();
        if(checkConnection() == null)
            return;
        ((Stage)newConnectionDialog.getScene().getWindow()).close();
        Main.getMainController().createNewSessionTab(connection);
    }

    @FXML
    public void closeDialog(){
        ((Stage)newConnectionDialog.getScene().getWindow()).close();
    }

    private void addNewConnection(Connection sc){
        Main.savedConnections.add(sc);
        savedConnection.getItems().add(sc.getName());
    }

    @FXML
    public void addDriver(){
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(newConnectionDialog.getScene().getWindow());
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("/view/newDriver.fxml"), resources);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        Scene dialogScene = new Scene(root);
        dialog.setTitle(resources.getString("newDriver.title"));
        dialog.setScene(dialogScene);
        dialog.show();
    }

    @FXML
    public void testConnection(){
        if(!checkFields());
        else if(checkConnection() == null){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle(resources.getString("newConnection.test.title"));
            alert.setContentText(resources.getString("newConnection.test.wrong.content"));
            alert.show();
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(resources.getString("newConnection.test.title"));
            alert.setContentText(resources.getString("newConnection.test.correct.content"));
            alert.show();
        }
    }

    private Connection checkConnection(){
        Connection connection = new Connection();
        connection.setDriver(Main.drivers.getDriver(driverList.getValue()));
        connection.setUrl(url.getText());
        connection.setUser(user.getText());
        connection.setPassword(password.getText());
        if(Connector.testConnection(connection))
            return connection;
        else return null;
    }

    private boolean checkFields(){
        if(Util.isNullOrEmpty(driverList.getValue()) ||
                Util.isNullOrEmpty(url.getText()) ||
                Util.isNullOrEmpty(user.getText()) ||
                Util.isNullOrEmpty(password.getText())) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(resources.getString("newDriver.error.title"));
            alert.setContentText(resources.getString("newDriver.error.content"));
            alert.show();
            return false;
        }
        return true;
    }
}
