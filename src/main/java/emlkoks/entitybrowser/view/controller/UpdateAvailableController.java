package emlkoks.entitybrowser.view.controller;

import emlkoks.entitybrowser.update.ReleaseInfo;
import emlkoks.entitybrowser.update.Updater;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class UpdateAvailableController implements Initializable {

    @FXML private BorderPane mainPane;
    @FXML private Label title;
    @FXML private Label content;
    @FXML private ResourceBundle resourceBundle;
    private Updater updater;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
    }

    public void showReleaseInfo(Updater updater, ReleaseInfo releaseInfo) {
        this.updater = updater;
        content.setText(releaseInfo.getReleaseNotes());
        title.setText(releaseInfo.getReleaseTitle());
    }

    @FXML
    public void update() {
        updater.update();

    }

    @FXML
    private void cancel() {
        ((Stage)mainPane.getScene().getWindow()).close();
    }
}
