package emlkoks.entitybrowser.session.entity;

import emlkoks.entitybrowser.session.exception.LibraryFileNotFoundException;
import org.junit.Test;

public class EntityListTest {

    @Test(expected = LibraryFileNotFoundException.class)
    public void createEntityListOfNull() throws LibraryFileNotFoundException {
        new EntityList(null);
    }
}