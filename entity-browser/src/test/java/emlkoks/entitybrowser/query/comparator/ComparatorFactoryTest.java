package emlkoks.entitybrowser.query.comparator;

import emlkoks.entitybrowser.Main;
import emlkoks.entitybrowser.session.entity.ClassDetails;
import org.junit.BeforeClass;
import org.junit.Test;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.*;

public class ComparatorFactoryTest {

    @BeforeClass
    public static void mockResources() {
        Main.resources = ResourceBundle.getBundle("lang.lang", new Locale("pl"));
    }

    @Test
    public void getBooleanComparator() {
        class Test {
            Boolean bigBoolean;
            boolean smallBoolean;
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
            Character bigCharacter;
            char smallChar;
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
            Date date;
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
            TestEnum testEnum;
        }
        var classDetails = new ClassDetails(Test.class);
        assertThat(ComparatorFactory.getComparator(classDetails.getFieldProperty("testEnum")),
                instanceOf(EnumComparator.class));
    }

    @Test
    public void getNumberComparator() {
        class Test {
            Integer bigInteger;
            Float bigFloat;
            Long bigLong;
            Double bigDouble;
            Short bigShort;
            int smallInteger;
            float smallFloat;
            long smallLong;
            double smallDouble;
            short smallShort;
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
            String string;
        }
        var classDetails = new ClassDetails(Test.class);
        assertThat(ComparatorFactory.getComparator(classDetails.getFieldProperty("string")),
                instanceOf(StringComparator.class));
    }
}