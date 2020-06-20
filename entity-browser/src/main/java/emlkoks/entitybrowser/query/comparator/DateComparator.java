package emlkoks.entitybrowser.query.comparator;

import emlkoks.entitybrowser.query.FieldFilter;
import java.util.Date;
import javafx.scene.Node;
import javafx.scene.control.DatePicker;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;

/**
 * Created by EmlKoks on 15.06.19.
 */
public class DateComparator extends Comparator {

    DateComparator() {
        comparationTypes.add(ComparationType.EQUAL);
        comparationTypes.add(ComparationType.NOT_EQUAL);
        comparationTypes.add(ComparationType.GREATER);
        comparationTypes.add(ComparationType.GREATER_OR_EQUAL);
        comparationTypes.add(ComparationType.LESS);
        comparationTypes.add(ComparationType.LESS_OR_EQUAL);
        comparationTypes.add(ComparationType.BETWEEN);
    }

    @Override
    public boolean canUseForClass(Class<?> clazz) {
        return Date.class.isAssignableFrom(clazz);
    }

    @Override
    protected Node createFieldValueField(Class<?> clazz) {
        return new DatePicker();
    }

    @Override
    protected Predicate createCustomPredicate(CriteriaBuilder cb, Path attributePath, FieldFilter fieldFilter) {
        return null;//TODO
    }
}
