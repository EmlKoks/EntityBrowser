package emlkoks.entitybrowser.connection;

import emlkoks.entitybrowser.common.Marshaller;
import emlkoks.entitybrowser.common.Util;
import emlkoks.entitybrowser.common.Resources;
import java.util.Objects;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.Data;

/**
 * Created by EmlKoks on 19.03.17.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class SavedConnections {
    @XmlElement(name = "connection")
    private ObservableList<Connection> list = FXCollections.observableArrayList();
    private Integer connectionLastId = 0;

    public Connection newConnection() {
        Connection newConnection = new Connection();
        newConnection.setName(getNewConnectionName());
        newConnection.setId(++connectionLastId);
        newConnection.setProvider(Util.DEFAULT_PROVIDER);
        list.add(newConnection);
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

    public void saveConnection(Connection connection) {
        if (Objects.nonNull(connection.getId())) {
            list.removeIf(conn -> connection.getId().equals(conn.getId()));
        } else {
            connection.setId(++connectionLastId);
        }
        list.add(connection);
        save();
    }

    public boolean exists(String name) {
        return list.stream()
                .anyMatch(connection -> connection.getName().equals(name));
    }

    public void remove(Integer id) {
        for (Connection connection : list) {
            if (id.equals(connection.getId())) {
                list.remove(connection);
                break;
            }
        }
        save();
    }

    public void save() {
        Marshaller.marshal(this, Resources.SAVED_CONNECTION);
    }
}
