package emlkoks.entitybrowser.query.comparator;

import java.util.Date;

/**
 * Created by EmlKoks on 15.06.19.
 */
public class ComparatorFactory {

    public AbstractComparator getExpression(Class clazz) {
        if(clazz == String.class) {
            return new StringComparator();
        }

        if(clazz == Date.class) {
            return new DateComparator();
        }

        if(clazz == boolean.class || clazz == Boolean.class) {
            return new BooleanComparator();
        }

        if(clazz.isAssignableFrom(Number.class) || clazz == int.class || clazz == float.class
                || clazz == long.class || clazz == double.class) {
            return new NumberComparator();
        }

        return null;
    }
}
