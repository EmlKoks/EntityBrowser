package emlkoks.entitybrowser.session.entity;

import emlkoks.entitybrowser.session.exception.LibraryFileNotFoundException;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

public class EntityListTest {
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
        assertEquals(2, entityList.getClasses().size());
    }

    @Test
    public void getClassNames() throws LibraryFileNotFoundException {
        var entityList = new EntityList(testLibFile);
        assertEquals(2, entityList.getClassNames().size());
    }

    @Test
    public void getClassNamesContainsCorrectnames() throws LibraryFileNotFoundException {
        var entityList = new EntityList(testLibFile);
        assertTrue(entityList.getClassNames().contains("test.TestEntity"));
    }

    @Test
    public void getEntity() throws LibraryFileNotFoundException {
        var entityList = new EntityList(testLibFile);
        assertNotNull(entityList.getEntity("test.TestEntity"));
    }


}