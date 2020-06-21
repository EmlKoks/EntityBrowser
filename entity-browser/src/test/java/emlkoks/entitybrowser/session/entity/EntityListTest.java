package emlkoks.entitybrowser.session.entity;

import emlkoks.entitybrowser.session.exception.LibraryFileNotFoundException;
import java.io.File;
import java.net.URISyntaxException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class EntityListTest {
    public static String TEST_EMPTY_LIB = "testLibWithoutEntities.jar";
    private File testLibFile;

    @Before
    public void loadTestLibFile() {
        testLibFile = new File(EntityLibraryLoaderTest.TEST_JAR_PATH);
    }

    @Test(expected = LibraryFileNotFoundException.class)
    public void createEntityListOfNull() throws LibraryFileNotFoundException {
        new EntityList(null);
    }

    @Test
    public void getClasses() throws LibraryFileNotFoundException {
        var entityList = new EntityList(testLibFile);
        assertEquals(7, entityList.getClasses().size());
    }

    @Test
    public void getClassesDetails() throws LibraryFileNotFoundException {
        var entityList = new EntityList(testLibFile);
        assertEquals(7, entityList.getClassesDetails().size());
    }

    @Test
    public void getClassNamesContainsCorrectnames() throws LibraryFileNotFoundException {
        var entityList = new EntityList(testLibFile);
        assertTrue(entityList.getClassesDetails().stream()
                .anyMatch(classDetails -> "test.IdEntity".equals(classDetails.getFullName())));
    }

    @Test
    public void getEntity() throws LibraryFileNotFoundException {
        var entityList = new EntityList(testLibFile);
        assertNotNull(entityList.getEntity("test.IdEntity"));
    }

    @Test
    public void hasClasses() throws LibraryFileNotFoundException {
        var entityList = new EntityList(testLibFile);
        assertTrue(entityList.hasClasses());
    }

    @Test
    public void hasNotClasses() throws LibraryFileNotFoundException, URISyntaxException {
        var libFile = new File(getClass().getClassLoader().getResource(TEST_EMPTY_LIB).toURI());
        var entityList = new EntityList(libFile);
        assertFalse(entityList.hasClasses());
    }
}