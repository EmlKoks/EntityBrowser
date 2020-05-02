package emlkoks.entitybrowser.view.control;

import emlkoks.entitybrowser.connection.Connection;
import javafx.scene.control.ListCell;

import java.util.Objects;

public class ConnectionListCell extends ListCell<Connection> {

    @Override
    protected void updateItem(Connection connection, boolean empty) {
        super.updateItem(connection, empty);
        if (empty || Objects.isNull(connection)) {
            setText(null);
        } else {
            setText(connection.getName());
        }
    }
}
