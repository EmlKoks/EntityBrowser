package emlkoks.entitybrowser.connection;

import emlkoks.entitybrowser.common.Marshaller;
import emlkoks.entitybrowser.common.Resources;
import java.util.Objects;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by EmlKoks on 19.03.17.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Getter
@Setter
public class SavedConnections {
    @XmlElement(name = "connection")
    private ObservableList<Connection> connections = FXCollections.observableArrayList();
    private Integer connectionLastId = 0;

    public Connection newConnection() {
        Connection newConnection = new Connection();
        newConnection.setName(getNewConnectionName());
        newConnection.setId(++connectionLastId);
        newConnection.setProvider(Provider.DEFAULT);
        connections.add(newConnection);
        save();
        return newConnection.clone();
    }

    private String getNewConnectionName() {
        String name = "new";
        while (exists(name)) {
            name += "1";
        }
        return name;
    }

    public boolean saveConnection(Connection connection) {
        if (Objects.isNull(connection)) {
            return false;
        }
        if (Objects.nonNull(connection.getId())) {
            connections.removeIf(conn -> connection.getId().equals(conn.getId()));
        } else {
            connection.setId(++connectionLastId);
        }
        connections.add(connection);
        save();
        return true;
    }

    public boolean exists(String name) {
        return connections.stream()
                .anyMatch(connection -> connection.getName().equals(name));
    }

    public boolean remove(Integer id) {
        for (Connection connection : connections) {
            if (id.equals(connection.getId())) {
                connections.remove(connection);
                save();
                return true;
            }
        }
        return false;
    }

    public void setupConnectionLastId() {
        connectionLastId = connections.stream()
                .mapToInt(Connection::getId)
                .max()
                .orElse(0);
    }

    public void save() {
        Marshaller.marshal(this, Resources.SAVED_CONNECTIONS_PATH);
    }
}
