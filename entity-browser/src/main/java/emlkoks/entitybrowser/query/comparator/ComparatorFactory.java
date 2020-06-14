package emlkoks.entitybrowser.query.comparator;

import com.google.common.collect.Sets;
import emlkoks.entitybrowser.session.entity.FieldProperty;
import java.util.Set;
import lombok.experimental.UtilityClass;

/**
 * Created by EmlKoks on 15.06.19.
 */
@UtilityClass
public class ComparatorFactory {
    private static Set<AbstractComparator<?>> comparators = Sets.newHashSet(
        new BooleanComparator(),
        new CharacterComparator(),
        new DateComparator(),
        new EnumComparator(),
        new NumberComparator(),
        new StringComparator());

    public AbstractComparator<?> getComparator(FieldProperty fieldProperty) {
        return getComparator(fieldProperty.getType());
    }

    private AbstractComparator<?> getComparator(Class<?> clazz) {
        return comparators.stream()
                .filter(comparator -> comparator.canUseForClass(clazz))
                .findFirst()
                .orElse(null);
    }
}
