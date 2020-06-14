package emlkoks.entitybrowser.connection.provider;

import emlkoks.entitybrowser.connection.Connection;
import emlkoks.entitybrowser.connection.ConnectionTest;
import emlkoks.entitybrowser.connection.Driver;
import emlkoks.entitybrowser.connection.Property;
import emlkoks.entitybrowser.connection.Provider;
import emlkoks.entitybrowser.session.entity.EntityLibraryLoaderTest;
import emlkoks.entitybrowser.session.entity.EntityList;
import emlkoks.entitybrowser.session.entity.EntityListTest;
import emlkoks.entitybrowser.session.exception.LibraryFileNotFoundException;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.net.URISyntaxException;

import static org.junit.Assert.*;

public class HibernateProviderTest {
    private File testLibFile;

    @Before
    public void loadTestLibFile() {
        testLibFile = new File(EntityLibraryLoaderTest.TEST_JAR_PATH);
    }

    @Test(expected = RuntimeException.class)
    public void createProviderWithoutConnectionDriver() {
        var connection = new Connection();
        connection.setUrl("url");
        new HibernateProvider(connection);
    }

    @Test(expected = RuntimeException.class)
    public void createProviderWithEmptyDriverClassName() {
        var driver = new Driver();
        var connection = new Connection();
        connection.setDriver(driver);
        new HibernateProvider(connection);
    }

    @Test(expected = RuntimeException.class)
    public void createProviderWithoutConnectionUrl() {
        var driver = new Driver();
        driver.setClassName("className");
        var connection = new Connection();
        connection.setDriver(driver);
        new HibernateProvider(connection);
    }

    @Test
    public void createHibernateProvider() {
        var driver = new Driver();
        driver.setClassName("className");
        var connection = new Connection();
        connection.setDriver(driver);
        connection.setUrl("url");

        assertNotNull(new HibernateProvider(connection));
    }

    @Test(expected = NullPointerException.class)
    public void connectProviderWithNullEntityList() {
        var driver = new Driver();
        driver.setClassName("className");
        var connection = new Connection();
        connection.setDriver(driver);
        connection.setUrl("url");
        JpaProvider provider = new HibernateProvider(connection);

        assertTrue(provider.connect(null));
    }

    @Test(expected = ProviderException.class)
    public void connectProviderWithWrongDriverClassName() throws LibraryFileNotFoundException {
        var driver = new Driver();
        driver.setClassName("className");
        var connection = new Connection();
        connection.setDriver(driver);
        connection.setUrl("url");
        JpaProvider provider = new HibernateProvider(connection);

        assertTrue(provider.connect(new EntityList(testLibFile)));
    }

    @Test
    public void connectProviderWithEmptyLib() throws LibraryFileNotFoundException, URISyntaxException {
        JpaProvider provider = new HibernateProvider(ConnectionTest.createH2Connection());
        var libFile = new File(getClass().getClassLoader().getResource(EntityListTest.TEST_EMPTY_LIB).toURI());
        var entityList = new EntityList(libFile);

        assertTrue(provider.connect(entityList));
    }

    @Test
    public void connectProviderWithNotEmptyLib() throws LibraryFileNotFoundException {
        JpaProvider provider = new HibernateProvider(ConnectionTest.createH2Connection());
        var entityList = new EntityList(testLibFile);
        assertTrue(provider.connect(entityList));
    }

    @Test
    public void getEntityManager() throws LibraryFileNotFoundException {
        JpaProvider provider = new HibernateProvider(ConnectionTest.createH2Connection());
        var entityList = new EntityList(testLibFile);
        provider.connect(entityList);
        assertNotNull(provider.getEntityManager());
    }

    @Test
    public void getCriteriaBuilder() throws LibraryFileNotFoundException {
        JpaProvider provider = new HibernateProvider(ConnectionTest.createH2Connection());
        var entityList = new EntityList(testLibFile);
        provider.connect(entityList);
        assertNotNull(provider.getCriteriaBuilder());
    }



    public static Connection createH2ConnectionWithTestLibrary() {
        var connection = new Connection();
        connection.setDriver(ConnectionTest.createH2Driver());
        connection.setUrl("jdbc:h2:mem:test");
        connection.getProperties().add(new Property("hibernate.hbm2ddl.auto", "create"));
        connection.setLibraryPath(EntityLibraryLoaderTest.TEST_JAR_PATH);
        connection.setProvider(Provider.Hibernate);
        return connection;
    }
}