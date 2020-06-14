package emlkoks.entitybrowser.connection;

import com.sun.javafx.collections.ImmutableObservableList;
import emlkoks.entitybrowser.Main;
import org.junit.Test;

import static emlkoks.entitybrowser.session.entity.EntityListTest.TEST_EMPTY_LIB;
import static org.junit.Assert.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.spy;

public class ConnectionTest {

    @Test
    public void setId() {
        var connection = new Connection();
        connection.setId(9);
        assertEquals(9, (int) connection.getId());
    }

    @Test
    public void setName() {
        var connection = new Connection();
        connection.setName("test");
        assertEquals("test", connection.getName());
    }

    @Test
    public void setDriver() {
        var driver = new Driver();
        var connection = new Connection();
        connection.setDriver(driver);
        assertEquals(driver, connection.getDriver());
    }

    @Test
    public void setDriverS() {
        var driver = new Driver();
        driver.setName("testDriver");
        Main.drivers.add(driver);

        var connection = new Connection();
        connection.setDriverS("testDriver");

        assertEquals(driver, connection.getDriver());
        assertEquals("testDriver", connection.getDriverS());
    }

    @Test
    public void setUrl() {
        var connection = new Connection();
        connection.setUrl("url");
        assertEquals("url", connection.getUrl());
    }

    @Test
    public void setUser() {
        var connection = new Connection();
        connection.setUser("user");
        assertEquals("user", connection.getUser());
    }

    @Test
    public void setPassword() {
        var connection = new Connection();
        connection.setPassword("password");
        assertEquals("password", connection.getPassword());
    }

    @Test
    public void setEmptyLibraryPath() {
        var connection = new Connection();
        assertFalse(connection.setLibraryPath(null));
        assertFalse(connection.setLibraryPath(""));
    }

    @Test
    public void setWrongLibraryPath() {
        var connection = new Connection();
        assertFalse(connection.setLibraryPath("libraryPath"));
    }

    @Test
    public void setLibraryPath() {
        var connection = new Connection();
        var testFile = getClass().getClassLoader().getResource(TEST_EMPTY_LIB).getPath();
        assertTrue(connection.setLibraryPath(testFile));
        assertEquals(testFile, connection.getLibraryPath());
    }

    @Test
    public void setProvider() {
        var connection = new Connection();
        connection.setProvider(Provider.Hibernate);
        assertEquals(Provider.Hibernate, connection.getProvider());
    }

    @Test
    public void setProperties() {
        var connection = new Connection();
        var properties = new ImmutableObservableList<>();
        connection.setProperties(new ImmutableObservableList<>());
        assertEquals(properties, connection.getProperties());
    }

    @Test
    public void testClone() {
        var connection = new Connection();
        connection.setId(9);
        connection.setName("testName");
        connection.setUrl("url");
        connection.setUser("user");
        connection.setPassword("password");
        var testFile = getClass().getClassLoader().getResource(TEST_EMPTY_LIB).getPath();
        connection.setLibraryPath(testFile);
        connection.setProvider(Provider.Hibernate);
        connection.setProperties(new ImmutableObservableList<>());
        var clonedConection = connection.clone();

        assertEquals(connection, clonedConection);
        assertNotSame(connection, clonedConection);
    }

    @Test
    public void wrongConnectionTest() {
        var driver = spy(createH2Driver());
        doNothing().when(driver).loadDriver();
        var connection = new Connection();
        connection.setDriver(driver);
        connection.setUrl("wrongUrl");

        assertFalse(connection.connectionTest());
    }

    @Test
    public void connectionTest() {
        var driver = spy(createH2Driver());
        doNothing().when(driver).loadDriver();
        var connection = createH2Connection();
        connection.setDriver(driver);

        assertTrue(connection.connectionTest());
    }

    public static Connection createH2Connection() {
        var connection = new Connection();
        connection.setDriver(createH2Driver());
        connection.setUrl("jdbc:h2:mem:test");
        connection.getProperties().add(new Property("hibernate.hbm2ddl.auto", "create"));
        return connection;
    }

    public static Driver createH2Driver() {
        var driver = new Driver();
        driver.setName("H2");
        driver.setClassName("org.h2.Driver");
        return driver;
    }
}