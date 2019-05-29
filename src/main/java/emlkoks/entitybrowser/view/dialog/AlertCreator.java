package emlkoks.entitybrowser.view.dialog;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;

import static javafx.scene.control.Alert.AlertType;

/**
 * Created by EmlKoks on 29.05.19.
 */
public abstract class AlertCreator extends DialogCreator<ButtonType> {

    private AlertType alertType;

    AlertCreator(String title, String message, AlertType alertType) {
        super(title, message);
        this.alertType = alertType;
    }

    AlertCreator(String message, AlertType alertType) {
        super(null, message);
        this.alertType = alertType;
    }

    @Override
    protected Dialog<ButtonType> createDialog() {
        Alert alert = new Alert(alertType);
        if(title != null && !title.isEmpty()) {
            alert.setTitle(title);
        }
        alert.setContentText(message);
        return alert;
    }
}
