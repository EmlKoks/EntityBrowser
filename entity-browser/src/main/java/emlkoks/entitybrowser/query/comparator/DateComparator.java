package emlkoks.entitybrowser.query.comparator;

import emlkoks.entitybrowser.query.FieldFilter;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
        return Date.class.isAssignableFrom(clazz)
                || LocalDate.class.isAssignableFrom(clazz)
                || LocalDateTime.class.isAssignableFrom(clazz); //TODO add other data types like DataTime
    }

    @Override
    public Node createFieldValueField(Class<?> clazz) {
        return new DatePicker();
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
