package emlkoks.entitybrowser.session.entity;

import org.junit.Test;

import javax.persistence.Id;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class EntityWrapperTest {

    @Test
    public void createByAllArgsConstructor() {
        class Test {}
        ClassDetails details = new ClassDetails(Test.class);
        EntityWrapper entityWrapper = new EntityWrapper(details, new Test());
        assertEquals(details, entityWrapper.getClassDetails());
    }

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

    @Test
    public void createDetailsTitleForNullId() {
        class Test {
            @Id
            private Long id;
        }
        Test test = new Test();
        EntityWrapper entityWrapper = new EntityWrapper(test);
        System.out.println(entityWrapper.createDetailsTitle());
        assertEquals("Test(Id: )", entityWrapper.createDetailsTitle());
    }
    @Test
    public void createDetailsTitleForNonNullId() {
        class Test {
            @Id
            private Long id;
        }
        Test test = new Test();
        test.id = 4L;
        EntityWrapper entityWrapper = new EntityWrapper(test);
        System.out.println(entityWrapper.createDetailsTitle());
        assertEquals("Test(Id: 4)", entityWrapper.createDetailsTitle());
    }

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