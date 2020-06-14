package emlkoks.entitybrowser.query.comparator;

import emlkoks.entitybrowser.query.FieldFilter;
import emlkoks.entitybrowser.query.comparator.comparation.ContainsComparation;
import emlkoks.entitybrowser.query.comparator.comparation.EqualComparation;
import emlkoks.entitybrowser.query.comparator.comparation.NotEqualComparation;
import javafx.scene.Node;
import javafx.scene.control.TextField;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;

/**
 * Created by EmlKoks on 11.06.19.
 */
public class StringComparator extends AbstractComparator<String> {

    StringComparator() {
        expressions.add(new ContainsComparation());
        expressions.add(new EqualComparation());
        expressions.add(new NotEqualComparation());
    }

    @Override
    public boolean canUseForClass(Class<?> clazz) {
        return clazz == String.class;
    }

    @Override
    protected Node createFieldValueField(Class<?> clazz) {
        return new TextField();
    }

    @Override
    public Predicate createPredicate(CriteriaBuilder cb, Path<String> attributePath, FieldFilter fieldFilter) {
        switch (fieldFilter.getComparation().getType()) {
            case CONTAINS:
                String value = "%" + fieldFilter.getValue() + "%";
                return cb.like(attributePath, value);
            case EQUAL:
                return cb.equal(attributePath, fieldFilter.getValue());
            case NOT_EQUAL:
                return cb.notEqual(attributePath, fieldFilter.getValue());
            default:
                throw new ExpressionNotImplementedException(fieldFilter.getComparation());
        }
    }
}
