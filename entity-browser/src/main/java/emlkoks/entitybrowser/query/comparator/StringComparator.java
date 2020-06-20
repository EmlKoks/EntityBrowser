package emlkoks.entitybrowser.query.comparator;

import emlkoks.entitybrowser.query.FieldFilter;
import javafx.scene.Node;
import javafx.scene.control.TextField;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;

/**
 * Created by EmlKoks on 11.06.19.
 */
public class StringComparator extends Comparator {

    StringComparator() {
        comparationTypes.add(ComparationType.CONTAINS);
        comparationTypes.add(ComparationType.EQUAL);
        comparationTypes.add(ComparationType.NOT_EQUAL);
    }

    @Override
    public boolean canUseForClass(Class<?> clazz) {
        return clazz == String.class;
    }

    @Override
    public Node createFieldValueField(Class<?> clazz) {
        return new TextField();
    }

    @Override
    protected Predicate createCustomPredicate(CriteriaBuilder cb, Path attributePath, FieldFilter fieldFilter) {
        switch (fieldFilter.getComparationType()) {
            case CONTAINS:
                String value = "%" + fieldFilter.getValue() + "%";
                return cb.like(attributePath, value);
            case EQUAL:
                return cb.equal(attributePath, fieldFilter.getValue());
            case NOT_EQUAL:
                return cb.notEqual(attributePath, fieldFilter.getValue());
            default:
                throw new ComparationTypeNotAllowedException(fieldFilter.getComparationType(), this);
        }
    }
}
