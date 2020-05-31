package emlkoks.entitybrowser.view.dialog;

import javafx.scene.control.Alert;

/**
 * Created by EmlKoks on 29.05.19.
 */
public class WarningDialogCreator extends AlertCreator {

    public WarningDialogCreator(String title, String message) {
        super(title, message, Alert.AlertType.WARNING);
    }

    public WarningDialogCreator(String message) {
        super(message, Alert.AlertType.WARNING);
    }
}
