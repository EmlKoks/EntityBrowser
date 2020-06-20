package emlkoks.entitybrowser.session;

import emlkoks.entitybrowser.connection.Connection;
import emlkoks.entitybrowser.connection.provider.HibernateProviderTest;
import emlkoks.entitybrowser.session.exception.LibraryFileNotFoundException;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class SessionTest {

    @Test(expected = NullPointerException.class)
    public void createSessionWithNullConenction() throws LibraryFileNotFoundException {
        new Session(null);
    }

    @Test(expected = LibraryFileNotFoundException.class)
    public void createSessionWithoutConnectionLibrary() throws LibraryFileNotFoundException {
        new Session(new Connection());
    }

    @Test
    public void createSessionWithTestH2Connection() throws LibraryFileNotFoundException {
        var session = new Session(HibernateProviderTest.createH2ConnectionWithTestLibrary());
        assertNotNull(session);
    }

    @Test
    public void connectSesion() throws LibraryFileNotFoundException {
        var session = new Session(HibernateProviderTest.createH2ConnectionWithTestLibrary());
        assertTrue(session.connect());
    }

}