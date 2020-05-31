package emlkoks.entitybrowser.view.dialog;

import java.util.Optional;
import javafx.scene.control.Dialog;

/**
 * Created by EmlKoks on 29.05.19.
 */
public abstract class DialogCreator<T> {
    protected String title;
    protected String message;

    DialogCreator(String title, String message) {
        this.title = title;
        this.message = message;
    }

    protected abstract Dialog<T> createDialog();

    public void show() {
        createDialog().show();
    }

    public Optional<T> showAndWait() {
        return createDialog().showAndWait();
    }
}
