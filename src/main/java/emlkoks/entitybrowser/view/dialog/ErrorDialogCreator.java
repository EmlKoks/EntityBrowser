package emlkoks.entitybrowser.view.dialog;

import javafx.scene.control.Alert;

/**
 * Created by EmlKoks on 29.05.19.
 */
public class ErrorDialogCreator extends AlertCreator{

    public ErrorDialogCreator(String title, String message){
        super(title, message, Alert.AlertType.ERROR);
    }

    public ErrorDialogCreator(String message){
        super(message, Alert.AlertType.ERROR);
    }
}
