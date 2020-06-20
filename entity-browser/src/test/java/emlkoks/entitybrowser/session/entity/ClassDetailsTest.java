package emlkoks.entitybrowser.session.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.Entity;
import javax.persistence.Id;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class ClassDetailsTest {

    @Test(expected = NullPointerException.class)
    public void nullClass() {
        new ClassDetails(null);
    }

    @Test
    public void getSimpleName() {
        ClassDetails classDetails = new ClassDetails(Object.class);
        assertEquals("Object", classDetails.getSimpleName());
    }

    @Test
    public void getFullName() {
        ClassDetails classDetails = new ClassDetails(Object.class);
        assertEquals("java.lang.Object", classDetails.getFullName());
    }

    @Test
    public void getClazz() {
        ClassDetails classDetails = new ClassDetails(Object.class);
        assertEquals(Object.class, classDetails.getClazz());
    }

    @Test
    public void getFieldsNamesClassWithoutFields() {
        class Test {}

        ClassDetails classDetails = new ClassDetails(Test.class);
        assertTrue(filterJacocoFields(classDetails.getFieldsNames()).isEmpty());
    }

    @Test
    public void getFieldsNamesClassWithFields() {
        class Test {
            private String f1;
            private int f2;
        }

        ClassDetails classDetails = new ClassDetails(Test.class);
        Set<String> fields = new HashSet<>();
        fields.add("f1");
        fields.add("f2");

        assertEquals(fields, filterJacocoFields(classDetails.getFieldsNames()));
    }

    private Set<String> filterJacocoFields(Set<String> fieldNames) {
        return fieldNames.stream()
                .filter(name -> !"$jacocoData".equals(name))//Added by JaCoco
                .collect(Collectors.toSet());
    }

    @Test
    public void getCorrectFieldProperty() {
        class Test {
            private String field;
        }

        ClassDetails classDetails = new ClassDetails(Test.class);

        assertEquals("field", classDetails.getFieldProperty("field").getName());
    }

    @Test
    public void getWrongFieldProperty() {
        class Test {
            private String field;
        }

        ClassDetails classDetails = new ClassDetails(Test.class);

        assertNotEquals("wrong", classDetails.getFieldProperty("field").getName());
    }

    @Test(expected = NullPointerException.class)
    public void getNullFieldProperty() {
        class Test {
            private String field;
        }

        ClassDetails classDetails = new ClassDetails(Test.class);

        classDetails.getFieldProperty(null);
    }

    @Test(expected = NullPointerException.class)
    public void getEmptyFieldProperty() {
        class Test {
            private String field;
        }

        ClassDetails classDetails = new ClassDetails(Test.class);

        classDetails.getFieldProperty("");
    }

    @Test
    public void getFieldPropertyFromEmptyClass() {
        class Test { }

        ClassDetails classDetails = new ClassDetails(Test.class);

        assertNull(classDetails.getFieldProperty("test"));
    }

    @Test
    public void getIdValueCorrect() {
        class Test {
            @Id
            public Long id;
        }

        Test test = new Test();
        test.id = 1L;
        ClassDetails classDetails = new ClassDetails(Test.class);
        assertEquals(1L, classDetails.getIdValue(new EntityWrapper(test)));
    }

    @Test
    public void getNullIdValue() {
        class Test {
            @Id
            public Long id;
        }

        Test test = new Test();
        ClassDetails classDetails = new ClassDetails(Test.class);
        assertNull(classDetails.getIdValue(new EntityWrapper(test)));
    }

    @Test
    public void getIdValueForNullObject() {
        class Test {
            @Id
            public Long id;
        }

        ClassDetails classDetails = new ClassDetails(Test.class);
        assertNull(classDetails.getIdValue(new EntityWrapper(null)));
    }

    @Test
    public void getSuperEntity() {
        class TestA {}

        class TestB extends TestA { }

        ClassDetails classDetails = new ClassDetails(TestB.class);
        assertEquals(TestA.class, classDetails.getSuperEntity().get().getClazz());
    }

    @Test
    public void getEnptySuperEntity() {
        class TestA {}

        ClassDetails classDetails = new ClassDetails(TestA.class);
        assertTrue(classDetails.getSuperEntity().isEmpty());
    }

    @Test
    public void isExcludedType() {
        assertTrue(new ClassDetails(Date.class).isExcludedType());
    }

    @Test
    public void isNotExcludedType() {
        assertFalse(new ClassDetails(List.class).isExcludedType());
        assertFalse(new ClassDetails(Map.class).isExcludedType());
        assertFalse(new ClassDetails(Set.class).isExcludedType());
    }

    @Test
    public void isSimplyType() {
        assertTrue(new ClassDetails(String.class).isSimplyType());
        assertTrue(new ClassDetails(Boolean.class).isSimplyType());
        assertTrue(new ClassDetails(Integer.class).isSimplyType());
        assertTrue(new ClassDetails(Long.class).isSimplyType());
        assertTrue(new ClassDetails(Float.class).isSimplyType());
        assertTrue(new ClassDetails(Byte.class).isSimplyType());
        assertTrue(new ClassDetails(Double.class).isSimplyType());
    }

    @Test
    public void isNotSimplyType() {
        class Test { }

        assertFalse(new ClassDetails(Date.class).isSimplyType());
        assertFalse(new ClassDetails(List.class).isSimplyType());
        assertFalse(new ClassDetails(Test.class).isSimplyType());
        assertFalse(new ClassDetails(Map.class).isSimplyType());
        assertFalse(new ClassDetails(Set.class).isSimplyType());
    }

    enum SimplyEnumTest {
        A, B
    }

    @Test
    public void isSimplyEnumType() {
        assertTrue(new ClassDetails(SimplyEnumTest.class).isSimplyType());
    }

    enum NotSimplyEnumTest {
        A, B;
        private String field;
    }

    @Test
    public void isNotSimplyEnumType() {
        assertFalse(new ClassDetails(NotSimplyEnumTest.class).isSimplyType());
    }

    @Test
    public void getEmptyFields() {
        class Test { }

        ClassDetails classDetails = new ClassDetails(Test.class);
        assertTrue(classDetails.getFields().isEmpty());
    }

    @Test
    public void checkNumberOfFields() {
        class Test {
            private String test1;
            private String test2;
            private String test3;
            private String test4;
        }

        ClassDetails classDetails = new ClassDetails(Test.class);
        assertEquals(4, classDetails.getFields().size());
    }

    @Test
    public void isEntity() {
        @Entity
        class Test { }

        ClassDetails classDetails = new ClassDetails(Test.class);
        assertTrue(classDetails.isEntity());
    }

    @Test
    public void isNotEntity() {
        class Test { }

        ClassDetails classDetails = new ClassDetails(Test.class);
        assertFalse(classDetails.isEntity());
    }
}