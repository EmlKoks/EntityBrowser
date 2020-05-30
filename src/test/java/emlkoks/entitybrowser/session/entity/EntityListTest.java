package emlkoks.entitybrowser.session.entity;

import emlkoks.entitybrowser.session.exception.LibraryFileCannotBeNull;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

public class EntityListTest {

    @Test(expected = LibraryFileCannotBeNull.class)
    public void createEntityListOfNull() {
        new EntityList(null);
    }

//    @Test
//    public void createEntityList() {
//        File testFile = new File("test");
//        new EntityList(testFile);
//    }
}