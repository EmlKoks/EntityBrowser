package emlkoks.entitybrowser.view.dialog;

import javafx.scene.control.Alert;

/**
 * Created by EmlKoks on 29.05.19.
 */
public class InformationDialogCreator extends AlertCreator {

    public InformationDialogCreator(String title, String message) {
        super(title, message, Alert.AlertType.INFORMATION);
    }

    public InformationDialogCreator(String message) {
        super(message, Alert.AlertType.INFORMATION);
    }
}
