package emlkoks.entitybrowser.entity;

import org.junit.Test;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class EntityWrapperTest {

    @Test
    public void getNullStringValue() {
        EntityWrapper entityWrapper = new EntityWrapper(null);
        assertEquals("[null]", entityWrapper.getStringValue());
    }

    @Test
    public void getStringStringValue() {
        EntityWrapper entityWrapper = new EntityWrapper("test");
        assertEquals("test", entityWrapper.getStringValue());
    }

    @Test
    public void getNumberStringValue() {
        EntityWrapper entityWrapper = new EntityWrapper(3);
        assertEquals("3", entityWrapper.getStringValue());
    }

    @Test
    public void getTooLongStringValue() {
        EntityWrapper entityWrapper = new EntityWrapper("Toooooooooooo looooooooong Stringgggggggg");
        assertEquals("Toooooooooooo looooooooong Str...", entityWrapper.getStringValue());
    }

    @Test(expected = NullPointerException.class)
    public void createDetailsTitleForNullValue() {
        EntityWrapper entityWrapper = new EntityWrapper(null);
        entityWrapper.createDetailsTitle();
    }

//    @Test(expected = NullPointerException.class)//TODO
//    public void createDetailsTitleForNullId() {
//        class Test {
//            @Id
//            long id;
//        }
//        Test test = new Test();
//        EntityWrapper entityWrapper = new EntityWrapper(test);
//        System.out.println(entityWrapper.createDetailsTitle());
//        assertEquals("Test(Id: )");
//    }

    @Test
    public void isNull() {
        EntityWrapper entityWrapper = new EntityWrapper(null);
        assertTrue(entityWrapper.isNull());
    }

    @Test
    public void isNotNull() {
        EntityWrapper entityWrapper = new EntityWrapper(new Object());
        assertFalse(entityWrapper.isNull());
    }

    @Test
    public void isIterable() {
        EntityWrapper entityWrapper = new EntityWrapper(new ArrayList<>());
        assertTrue(entityWrapper.isIterable());
    }

    @Test
    public void isNotIterable() {
        EntityWrapper entityWrapper = new EntityWrapper(new Object());
        assertFalse(entityWrapper.isIterable());
    }

    @Test
    public void getNullValue() {
        EntityWrapper entityWrapper = new EntityWrapper(null);
        assertNull(entityWrapper.getValue());
    }

    @Test
    public void getObjectValue() {
        Object obj = new Object();
        EntityWrapper entityWrapper = new EntityWrapper(obj);
        assertEquals(obj, entityWrapper.getValue());
    }
}