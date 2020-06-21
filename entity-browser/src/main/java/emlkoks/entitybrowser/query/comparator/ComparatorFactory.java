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
    private static Set<Comparator> comparators = Sets.newHashSet(
        new BooleanComparator(),
        new CharacterComparator(),
        new DateComparator(),
        new EnumComparator(),
        new NumberComparator(),
        new StringComparator());

    public Comparator getComparator(FieldProperty fieldProperty) {
        return comparators.stream()
                .filter(comparator -> comparator.canUseForClass(fieldProperty.getType()))
                .findFirst()
                .orElseThrow(() -> new ComparatorNotFoundException(fieldProperty.getType()));
    }
}
