package emlkoks.entitybrowser.session.entity;

import emlkoks.entitybrowser.session.exception.LibraryFileNotFoundException;
import org.junit.Test;
import java.io.File;

import static org.junit.Assert.*;

public class EntityLibraryLoaderTest {
    public final static String TEST_MODULE = "test-library";
    public final static String TEST_JAR_PATH = ".." + File.separatorChar + TEST_MODULE + File.separatorChar + "target" + File.separatorChar + TEST_MODULE + ".jar";

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
        var file = new File(TEST_JAR_PATH);
        var classes = new EntityLibraryLoader(file).load();
        classes.stream()
                .map(ClassDetails::getFullName)
                .forEach(System.out::println);
        assertEquals(2, classes.size());
    }
}