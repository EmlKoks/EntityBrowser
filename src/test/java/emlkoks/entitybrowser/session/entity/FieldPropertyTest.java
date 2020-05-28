package emlkoks.entitybrowser.session.entity;

import org.junit.Test;
import javax.persistence.Id;
import java.lang.reflect.Field;

import static org.junit.Assert.*;

public class FieldPropertyTest {

    @Test
    public void correctGetFieldValue() throws NoSuchFieldException {
        class TestId {
            @Id
            public long id;
        }

        TestId testObject = new TestId();
        testObject.id = 10L;
        EntityWrapper entityWrapper = new EntityWrapper(testObject);
        Field idField = TestId.class.getField("id");
        FieldProperty fieldProperty = new FieldProperty(idField);

        assertEquals(fieldProperty.getValue(entityWrapper).getValue(), 10L);
    }

    @Test
    public void fieldIsId() throws NoSuchFieldException {
        class TestId {
            @Id
            public long id;
        }

        Field idField = TestId.class.getField("id");
        FieldProperty fieldProperty = new FieldProperty(idField);

        assertTrue(fieldProperty.isId());
    }

    @Test
    public void fieldIsNotId() throws NoSuchFieldException {
        class TestId {
            public long notId;

        }

        Field notIdField = TestId.class.getField("notId");
        FieldProperty fieldProperty = new FieldProperty(notIdField);

        assertFalse(fieldProperty.isId());
    }

    @Test
    public void fieldIsFinal() throws NoSuchFieldException {
        class TestId {
            public final long finalField = 0;
        }

        Field finalField = TestId.class.getField("finalField");
        FieldProperty fieldProperty = new FieldProperty(finalField);

        assertTrue(fieldProperty.isFinal());
    }

    @Test
    public void fieldIsNotFinal() throws NoSuchFieldException {
        class TestId {
            public long notFinalField = 0;
        }

        Field notFinalField = TestId.class.getField("notFinalField");
        FieldProperty fieldProperty = new FieldProperty(notFinalField);

        assertFalse(fieldProperty.isId());
    }

    @Test
    public void getValueCorrect() throws NoSuchFieldException {
        class Test {
            public String testField;
        }
        Test test = new Test();
        test.testField = "test";
        EntityWrapper entityWrapper = new EntityWrapper(test);
        Field field = Test.class.getField("testField");
        FieldProperty fieldProperty = new FieldProperty(field);

        assertEquals("test", fieldProperty.getValue(entityWrapper).getValue());
    }

    @Test
    public void getValueOfNull() throws NoSuchFieldException {
        class Test {
            public String testField;
        }
        EntityWrapper entityWrapper = new EntityWrapper(null);
        Field field = Test.class.getField("testField");
        FieldProperty fieldProperty = new FieldProperty(field);

        assertNull(fieldProperty.getValue(entityWrapper).getValue());
    }

    @Test
    public void hasAnnotation() throws NoSuchFieldException {
        class TestId {
            @Deprecated
            public long annotation;
        }

        Field idField = TestId.class.getField("annotation");
        FieldProperty fieldProperty = new FieldProperty(idField);

        assertTrue(fieldProperty.hasAnnotation(Deprecated.class));
    }

    @Test
    public void hasntAnnotation() throws NoSuchFieldException {
        class TestId {
            public long noAnnotation;
        }

        Field notIdField = TestId.class.getField("noAnnotation");
        FieldProperty fieldProperty = new FieldProperty(notIdField);

        assertFalse(fieldProperty.hasAnnotation(Deprecated.class));
    }

    @Test
    public void getTypePrimitive() throws NoSuchFieldException {
        class TestId {
            public long testField;
        }

        Field field = TestId.class.getField("testField");
        FieldProperty fieldProperty = new FieldProperty(field);
        assertEquals(long.class, fieldProperty.getType());
    }

    @Test
    public void getTypeClass() throws NoSuchFieldException {
        class TestId {
            public Long testField;
        }

        Field field = TestId.class.getField("testField");
        FieldProperty fieldProperty = new FieldProperty(field);
        assertEquals(Long.class, fieldProperty.getType());
    }

}