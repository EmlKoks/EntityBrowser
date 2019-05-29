package emlkoks.entitybrowser.view.dialog;

import javafx.scene.control.Alert;

/**
 * Created by EmlKoks on 29.05.19.
 */
public class ConfirmationDialogCreator extends AlertCreator {

    public ConfirmationDialogCreator(String title, String message) {
        super(title, message, Alert.AlertType.CONFIRMATION);
    }

    public ConfirmationDialogCreator(String message) {
        super(message, Alert.AlertType.CONFIRMATION);
    }

}
