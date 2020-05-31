package emlkoks.entitybrowser.session.entity;

import emlkoks.entitybrowser.session.exception.LibraryFileNotFoundException;
import org.junit.Test;
import java.io.File;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Map;

import static org.junit.Assert.*;

public class EntityLibraryLoaderTest {
    private final static String TEST_MODULE = "test-library";

    @Test(expected = LibraryFileNotFoundException.class)
    public void createWithNull() throws LibraryFileNotFoundException {
        new EntityLibraryLoader(null);
    }

    @Test(expected = LibraryFileNotFoundException.class)
    public void createWithNotExistingFile() throws LibraryFileNotFoundException {
        new EntityLibraryLoader(new File("badUrl"));
    }

    @Test
    public void loadTestLib() throws LibraryFileNotFoundException {
        var file = new File(".." + File.separatorChar + TEST_MODULE + File.separatorChar + "target" + File.separatorChar + TEST_MODULE + ".jar");
        var classes = new EntityLibraryLoader(file).load();
        classes.stream()
                .map(ClassDetails::getFullName)
                .forEach(System.out::println);
        assertEquals(2, classes.size());
    }
}