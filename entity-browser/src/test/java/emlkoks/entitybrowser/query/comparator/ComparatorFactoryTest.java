package emlkoks.entitybrowser.query.comparator;

import emlkoks.entitybrowser.Main;
import emlkoks.entitybrowser.session.entity.ClassDetails;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;

public class ComparatorFactoryTest {

    @BeforeClass
    public static void mockResources() {
        Main.resources = ResourceBundle.getBundle("lang.lang", new Locale("pl"));
    }

    @Test(expected = ComparatorNotFoundException.class)
    public void notFoundComparator() {
        class Test {
            class NewType { }

            private NewType newType;
        }

        var classDetails = new ClassDetails(Test.class);
        ComparatorFactory.getComparator(classDetails.getFieldProperty("newType"));
    }

    @Test
    public void getBooleanComparator() {
        class Test {
            private Boolean bigBoolean;
            private boolean smallBoolean;
        }

        var classDetails = new ClassDetails(Test.class);
        assertThat(ComparatorFactory.getComparator(classDetails.getFieldProperty("bigBoolean")),
                instanceOf(BooleanComparator.class));
        assertThat(ComparatorFactory.getComparator(classDetails.getFieldProperty("smallBoolean")),
                instanceOf(BooleanComparator.class));
    }

    @Test
    public void getCharacterComparator() {
        class Test {
            private Character bigCharacter;
            private char smallChar;
        }

        var classDetails = new ClassDetails(Test.class);
        assertThat(ComparatorFactory.getComparator(classDetails.getFieldProperty("bigCharacter")),
                instanceOf(CharacterComparator.class));
        assertThat(ComparatorFactory.getComparator(classDetails.getFieldProperty("smallChar")),
                instanceOf(CharacterComparator.class));
    }

    @Test
    public void getDateComparator() {
        class Test {
            private Date date;
        }

        var classDetails = new ClassDetails(Test.class);
        assertThat(ComparatorFactory.getComparator(classDetails.getFieldProperty("date")),
                instanceOf(DateComparator.class));
    }

    enum TestEnum {
        A,B
    }

    @Test
    public void getEnumComparator() {
        class Test {
            private TestEnum testEnum;
        }

        var classDetails = new ClassDetails(Test.class);
        assertThat(ComparatorFactory.getComparator(classDetails.getFieldProperty("testEnum")),
                instanceOf(EnumComparator.class));
    }

    @Test
    public void getNumberComparator() {
        class Test {
            private Integer bigInteger;
            private Float bigFloat;
            private Long bigLong;
            private Double bigDouble;
            private Short bigShort;
            private int smallInteger;
            private float smallFloat;
            private long smallLong;
            private double smallDouble;
            private short smallShort;
        }

        var classDetails = new ClassDetails(Test.class);
        assertThat(ComparatorFactory.getComparator(classDetails.getFieldProperty("bigInteger")),
                instanceOf(NumberComparator.class));
        assertThat(ComparatorFactory.getComparator(classDetails.getFieldProperty("bigFloat")),
                instanceOf(NumberComparator.class));
        assertThat(ComparatorFactory.getComparator(classDetails.getFieldProperty("bigLong")),
                instanceOf(NumberComparator.class));
        assertThat(ComparatorFactory.getComparator(classDetails.getFieldProperty("bigDouble")),
                instanceOf(NumberComparator.class));
        assertThat(ComparatorFactory.getComparator(classDetails.getFieldProperty("bigShort")),
                instanceOf(NumberComparator.class));
        assertThat(ComparatorFactory.getComparator(classDetails.getFieldProperty("smallInteger")),
                instanceOf(NumberComparator.class));
        assertThat(ComparatorFactory.getComparator(classDetails.getFieldProperty("smallFloat")),
                instanceOf(NumberComparator.class));
        assertThat(ComparatorFactory.getComparator(classDetails.getFieldProperty("smallLong")),
                instanceOf(NumberComparator.class));
        assertThat(ComparatorFactory.getComparator(classDetails.getFieldProperty("smallDouble")),
                instanceOf(NumberComparator.class));
        assertThat(ComparatorFactory.getComparator(classDetails.getFieldProperty("smallShort")),
                instanceOf(NumberComparator.class));
    }


    @Test
    public void getStringComparator() {
        class Test {
            private String string;
        }

        var classDetails = new ClassDetails(Test.class);
        assertThat(ComparatorFactory.getComparator(classDetails.getFieldProperty("string")),
                instanceOf(StringComparator.class));
    }
}