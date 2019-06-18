package emlkoks.entitybrowser.query.comparator;

import emlkoks.entitybrowser.session.FieldProperty;

import java.util.Date;

/**
 * Created by EmlKoks on 15.06.19.
 */
public class ComparatorFactory {

    public AbstractComparator getComparator(FieldProperty fieldProperty) {
        return getComparator(fieldProperty.getField().getType());
    }

    AbstractComparator getComparator(Class clazz) {
        if (clazz == String.class) {
            return new StringComparator();
        }

        if (clazz == Character.class || clazz == char.class) {
            return new CharacterComparator();
        }

        if (clazz == Date.class) {
            return new DateComparator();
        }

        if (clazz == boolean.class || clazz == Boolean.class) {
            return new BooleanComparator();
        }

        if (Number.class.isAssignableFrom(clazz) || clazz == int.class || clazz == float.class
                || clazz == long.class || clazz == double.class || clazz == short.class) {
            return new NumberComparator();
        }

        if (clazz.isEnum()) {
            return new EnumComparator();
        }

        return null;
    }
}
