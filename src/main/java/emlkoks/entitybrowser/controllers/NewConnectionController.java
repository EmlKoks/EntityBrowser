package emlkoks.entitybrowser.controllers;

import emlkoks.entitybrowser.Main;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Created by EmlKoks on 18.03.17.
 */
public class NewConnectionController implements Initializable{

    @FXML
    private ListView<String> savedConnection;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setSavedConnection();
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
        for(String name : Main.savedConnections.keySet())
            savedConnection.getItems().add(name);
    }
}
