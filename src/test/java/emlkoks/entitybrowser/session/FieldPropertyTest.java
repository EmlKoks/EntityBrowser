package emlkoks.entitybrowser.session;

import emlkoks.entitybrowser.entity.EntityWrapper;
import emlkoks.entitybrowser.entity.FieldProperty;
import org.junit.Assert;
import org.junit.Test;

import javax.persistence.Id;
import java.lang.reflect.Field;

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
        FieldProperty fieldProperty = new FieldProperty(idField, null);

        Assert.assertEquals(fieldProperty.getValue(entityWrapper).getValue(), 10L);
    }

    @Test
    public void fieldIsId() throws NoSuchFieldException {
        class TestId {
            @Id
            public long id;
        }

        Field idField = TestId.class.getField("id");
        FieldProperty fieldProperty = new FieldProperty(idField, null);

        Assert.assertTrue(fieldProperty.isId());
    }

    @Test
    public void fieldIsNotId() throws NoSuchFieldException {
        class TestId {
            public long notId;

        }

        Field notIdField = TestId.class.getField("notId");
        FieldProperty fieldProperty = new FieldProperty(notIdField, null);

        Assert.assertFalse(fieldProperty.isId());
    }

    @Test
    public void fieldIsFinal() throws NoSuchFieldException {
        class TestId {
            public final long finalField = 0;
        }

        Field finalField = TestId.class.getField("finalField");
        FieldProperty fieldProperty = new FieldProperty(finalField, null);

        Assert.assertTrue(fieldProperty.isFinal());
    }

    @Test
    public void fieldIsNotFinal() throws NoSuchFieldException {
        class TestId {
            public long notFinalField = 0;
        }

        Field notFinalField = TestId.class.getField("notFinalField");
        FieldProperty fieldProperty = new FieldProperty(notFinalField, null);

        Assert.assertFalse(fieldProperty.isId());
    }

}