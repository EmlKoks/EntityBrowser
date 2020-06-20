package emlkoks.entitybrowser.connection;

import java.util.Arrays;
import java.util.List;
import javafx.collections.FXCollections;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class SavedConnectionsTest {
    private SavedConnections savedConnections;

    @Before
    public void initSavedConnection() {
        savedConnections = new SavedConnections();
    }


    @Test
    public void createNewConnection() {
        var conection = savedConnections.newConnection();
        assertEquals("new", conection.getName());
        assertEquals(1, (int)conection.getId());
        assertEquals(Provider.DEFAULT, conection.getProvider());
    }

    @Test
    public void createMoreNewConnections() {
        var conection1 = savedConnections.newConnection();
        assertEquals("new", conection1.getName());
        assertEquals(1, (int)conection1.getId());
        var conection2 = savedConnections.newConnection();
        assertEquals("new1", conection2.getName());
        assertEquals(2, (int)conection2.getId());
        var conection3 = savedConnections.newConnection();
        assertEquals("new11", conection3.getName());
        assertEquals(3, (int)conection3.getId());
    }

    @Test
    public void saveNullConnection() {
        assertFalse(savedConnections.saveConnection(null));
    }

    @Test
    public void saveEmptyConnection() {
        var connection = new Connection();
        assertNull(connection.getId());
        assertTrue(savedConnections.saveConnection(connection));
        assertEquals(1, (int)connection.getId());
    }

    @Test
    public void overrideConnection() {
        var connection = savedConnections.newConnection();
        assertTrue(savedConnections.saveConnection(connection));
    }

    @Test
    public void removeExistingConnection() {
        var connection = savedConnections.newConnection();
        assertTrue(savedConnections.remove(connection.getId()));
    }

    @Test
    public void removeNotExistingConnection() {
        var connection = savedConnections.newConnection();
        assertFalse(savedConnections.remove(connection.getId() + 1));
    }

    @Test
    public void setupConnectionLastIdWithEmptyConnections() {
        savedConnections.setupConnectionLastId();
        assertEquals(0, (int)savedConnections.getConnectionLastId());
    }

    @Test
    public void setConnections() {
        List<Connection> connections = Arrays.asList(new Connection(), new Connection(), new Connection());
        savedConnections.setConnections(FXCollections.observableList(connections));
        assertEquals(3, savedConnections.getConnections().size());
    }

    @Test
    public void setupConnectionLastIdWithNonEmptyConnections() {
        savedConnections.newConnection();
        savedConnections.newConnection();
        var thirdConnection = savedConnections.newConnection();
        savedConnections.setupConnectionLastId();
        assertEquals(3, (int)savedConnections.getConnectionLastId());
        assertEquals(thirdConnection.getId(), savedConnections.getConnectionLastId());
    }

    @Test
    public void setConnectionLastId() {
        savedConnections.setConnectionLastId(5);
        assertEquals(5, (int)savedConnections.getConnectionLastId());
    }
}