package emlkoks.entitybrowser.session.entity;

import org.junit.Test;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class EntityDetailsTest {

    @Test(expected = NullPointerException.class)
    public void nullClass() {
        new EntityDetails(null);
    }

    @Test
    public void getSimpleName() {
        EntityDetails entityDetails = new EntityDetails(Object.class);
        assertEquals("Object", entityDetails.getSimpleName());
    }

    @Test
    public void getFullName() {
        EntityDetails entityDetails = new EntityDetails(Object.class);
        assertEquals("java.lang.Object", entityDetails.getFullName());
    }

    @Test
    public void getClazz() {
        EntityDetails entityDetails = new EntityDetails(Object.class);
        assertEquals(Object.class, entityDetails.getClazz());
    }

    @Test
    public void getFieldsNamesClassWithoutFields() {
        class Test {}

        EntityDetails entityDetails = new EntityDetails(Test.class);
        assertTrue(filterJacocoFields(entityDetails.getFieldsNames()).isEmpty());
    }

    @Test
    public void getFieldsNamesClassWithFields() {
        class Test {
            String f1;
            int f2;
        }

        EntityDetails entityDetails = new EntityDetails(Test.class);
        Set<String> fields = new HashSet<>();
        fields.add("f1");
        fields.add("f2");

        assertEquals(fields, filterJacocoFields(entityDetails.getFieldsNames()));
    }

    private Set<String> filterJacocoFields(Set<String>fieldNames) {
        return fieldNames.stream()
                .filter(name -> !"$jacocoData".equals(name))//Added by JaCoco
                .collect(Collectors.toSet());
    }

    @Test
    public void getCorrectFieldProperty() {
        class Test {
            String field;
        }
        EntityDetails entityDetails = new EntityDetails(Test.class);

        assertEquals("field", entityDetails.getFieldProperty("field").getName());
    }

    @Test
    public void getWrongFieldProperty() {
        class Test {
            String field;
        }
        EntityDetails entityDetails = new EntityDetails(Test.class);

        assertNotEquals("wrong", entityDetails.getFieldProperty("field").getName());
    }

    @Test(expected = NullPointerException.class)
    public void getNullFieldProperty() {
        class Test {
            String field;
        }
        EntityDetails entityDetails = new EntityDetails(Test.class);

        entityDetails.getFieldProperty(null);
    }

    @Test(expected = NullPointerException.class)
    public void getEmptyFieldProperty() {
        class Test {
            String field;
        }
        EntityDetails entityDetails = new EntityDetails(Test.class);

        entityDetails.getFieldProperty("");
    }

    @Test
    public void getFieldPropertyFromEmptyClass() {
        class Test { }
        EntityDetails entityDetails = new EntityDetails(Test.class);

        assertNull(entityDetails.getFieldProperty("test"));
    }
}