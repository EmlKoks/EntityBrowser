package emlkoks.entitybrowser.session.entity;

import emlkoks.entitybrowser.session.exception.ClassCannotBeNullException;
import org.junit.Test;
import javax.persistence.Transient;

import static org.junit.Assert.*;

public class EntityFieldsTest {

    @Test(expected = ClassCannotBeNullException.class)
    public void createWithNull() {
        new EntityFields(null);
    }

    @Test
    public void correctGetFields() {
        class Test { }
        EntityFields entityFields = new EntityFields(new ClassDetails(Test.class));
        assertTrue(entityFields.get().isEmpty());
    }

    @Test
    public void createWithSkippedFields() {
        class Test {
            private final String finalField = "";
            @Transient
            private String transientField;
            private String serialVersionUID;
        }
        EntityFields entityFields = new EntityFields(new ClassDetails(Test.class));
        assertTrue(entityFields.get().isEmpty());
    }

}