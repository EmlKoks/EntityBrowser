package emlkoks.entitybrowser.query.comparator;

import emlkoks.entitybrowser.session.entity.FieldProperty;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by EmlKoks on 15.06.19.
 */
public class ComparatorFactory {
    private static Set<AbstractComparator<?>> comparators = new HashSet<>();

    static {
        comparators.add(new BooleanComparator());
        comparators.add(new CharacterComparator());
        comparators.add(new DateComparator());
        comparators.add(new EnumComparator());
        comparators.add(new NumberComparator());
        comparators.add(new StringComparator());
    }

    public static AbstractComparator<?> getComparator(FieldProperty fieldProperty) {
        return getComparator(fieldProperty.getType());
    }

    static AbstractComparator<?> getComparator(Class<?> clazz) {
        return comparators.stream()
                .filter(comparator -> comparator.canUseForClass(clazz))
                .findFirst()
                .orElse(null);
    }
}
