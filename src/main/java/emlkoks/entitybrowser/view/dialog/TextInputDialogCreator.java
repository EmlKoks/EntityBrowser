package emlkoks.entitybrowser.view.dialog;

import javafx.scene.control.Dialog;
import javafx.scene.control.TextInputDialog;

/**
 * Created by EmlKoks on 29.05.19.
 */
public class TextInputDialogCreator extends DialogCreator<String> {

    public TextInputDialogCreator(String title, String message) {
        super(title, message);
    }

    public TextInputDialogCreator(String message) {
        super(null, message);
    }

    @Override
    protected Dialog<String> createDialog() {
        TextInputDialog dialog = new TextInputDialog();
        if (title != null && !title.isEmpty()) {
            dialog.setTitle(title);
        }
        dialog.setContentText(message);
        return dialog;
    }
}
