package emlkoks.entitybrowser.query.comparator;

import emlkoks.entitybrowser.query.FieldFilter;
import emlkoks.entitybrowser.view.control.field.NumberTextField;
import javafx.scene.Node;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;

/**
 * Created by EmlKoks on 15.06.19.
 */
public class NumberComparator extends Comparator {

    NumberComparator() {
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
        return clazz == int.class
                || clazz == float.class
                || clazz == long.class
                || clazz == double.class
                || clazz == short.class
                || Number.class.isAssignableFrom(clazz);
    }

    @Override
    public Node createFieldValueField(Class<?> clazz) {
        return new NumberTextField(clazz);
    }

    @Override
    protected Predicate createCustomPredicate(CriteriaBuilder cb, Path attributePath, FieldFilter fieldFilter) {
        switch (fieldFilter.getComparationType()) {
            case EQUAL:
                return cb.equal(attributePath, fieldFilter.getValue());
            case NOT_EQUAL:
                return cb.notEqual(attributePath, fieldFilter.getValue());
            case GREATER:
                return cb.greaterThan(attributePath, (Comparable) fieldFilter.getValue());
            case GREATER_OR_EQUAL:
                return cb.greaterThanOrEqualTo(attributePath, (Comparable) fieldFilter.getValue());
            case LESS:
                return cb.lessThan(attributePath, (Comparable) fieldFilter.getValue());
            case LESS_OR_EQUAL:
                return cb.lessThanOrEqualTo(attributePath, (Comparable) fieldFilter.getValue());
            case BETWEEN:
                var from = (Comparable) fieldFilter.getValue(0);
                var to = (Comparable) fieldFilter.getValue(1);
                return cb.between(attributePath, from, to);
            default:
                throw new ComparationTypeNotAllowedException(fieldFilter.getComparationType(), this);
        }
    }

}
