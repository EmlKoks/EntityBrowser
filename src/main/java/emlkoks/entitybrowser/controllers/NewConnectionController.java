package emlkoks.entitybrowser.controllers;

import emlkoks.entitybrowser.Main;
import emlkoks.entitybrowser.Util;
import emlkoks.entitybrowser.connection.SavedConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Created by EmlKoks on 18.03.17.
 */
public class NewConnectionController implements Initializable{

    @FXML
    BorderPane newConnectionDialog;

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
        setSavedConnection();
        driverList.getItems().addAll(Main.driverList.getDriverNames());
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
        for(SavedConnection sc : Main.savedConnections.getList())
            savedConnection.getItems().add(sc.getName());
    }

    @FXML
    public void saveConnection(ActionEvent event){

        while(true){
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("2");
            Optional<String> result = dialog.showAndWait();
            if(!result.isPresent()) return;
            if(Util.isNullOrEmpty(result.get())) continue;
            boolean exist = false;
            for(SavedConnection sc : Main.savedConnections.getList())
                if(sc.getName().equals(result.get())){
                    exist=true;
                    break;
                }
            if (!exist){
                SavedConnection newConnection = new SavedConnection();
                newConnection.setName(result.get());
                newConnection.setUrl(url.getText());
                newConnection.setUser(user.getText());
                newConnection.setPassword(password.getText());
                newConnection.setDriver(Main.driverList.getDriver(driverList.getValue()));
                addNewConnection(newConnection);
                return;
            }
        }
    }

    @FXML
    public void connect(ActionEvent event){
        ((Stage)newConnectionDialog.getScene().getWindow()).close();
    }

    @FXML
    public void closeDialog(ActionEvent event){
        ((Stage)newConnectionDialog.getScene().getWindow()).close();
    }

    private void addNewConnection(SavedConnection sc){
        Main.savedConnections.getList().add(sc);
        savedConnection.getItems().add(sc.getName());
    }
}
