package emlkoks.entitybrowser.session;

import emlkoks.entitybrowser.entity.EntityWrapper;
import org.junit.Assert;
import org.junit.Test;

import javax.persistence.Id;
import java.lang.reflect.Field;

public class FieldPropertyTest {
    class TestId {
        @Id
        public long id;
        public long notId;

        public final long finalField = 0;
        public long notFinalField = 0;
    }

    @Test
    public void correctGetFieldValue() throws NoSuchFieldException {
        TestId testObject = new TestId();
        testObject.id = 10L;
        EntityWrapper entityWrapper = new EntityWrapper(testObject);
        Field idField = TestId.class.getField("id");
        FieldProperty fieldProperty = new FieldProperty(idField, null);

        Assert.assertEquals(fieldProperty.getValue(entityWrapper).getValue(), 10L);
    }

    @Test
    public void fieldIsId() throws NoSuchFieldException {
        Field idField = TestId.class.getField("id");
        FieldProperty fieldProperty = new FieldProperty(idField, null);

        Assert.assertTrue(fieldProperty.isId());
    }

    @Test
    public void fieldIsNotId() throws NoSuchFieldException {
        Field notIdField = TestId.class.getField("notId");
        FieldProperty fieldProperty = new FieldProperty(notIdField, null);

        Assert.assertFalse(fieldProperty.isId());
    }

    @Test
    public void fieldIsFinal() throws NoSuchFieldException {
        Field finalField = TestId.class.getField("finalField");
        FieldProperty fieldProperty = new FieldProperty(finalField, null);

        Assert.assertTrue(fieldProperty.isFinal());
    }

    @Test
    public void fieldIsNotFinal() throws NoSuchFieldException {
        Field notFinalField = TestId.class.getField("notFinalField");
        FieldProperty fieldProperty = new FieldProperty(notFinalField, null);

        Assert.assertFalse(fieldProperty.isId());
    }

}