package emlkoks.entitybrowser.view.control;

import emlkoks.entitybrowser.connection.Connection;
import java.util.Objects;
import javafx.scene.control.ListCell;

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
