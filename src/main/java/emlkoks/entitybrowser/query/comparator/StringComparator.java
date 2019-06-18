package emlkoks.entitybrowser.query.comparator;

import emlkoks.entitybrowser.query.FieldFilter;
import emlkoks.entitybrowser.query.comparator.expression.ContainsExpression;
import emlkoks.entitybrowser.query.comparator.expression.EqualExpression;
import emlkoks.entitybrowser.query.comparator.expression.NotEqualExpression;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;

/**
 * Created by EmlKoks on 11.06.19.
 */
public class StringComparator extends AbstractComparator<String> {

    StringComparator() {
        expressions.add(new ContainsExpression());
        expressions.add(new EqualExpression());
        expressions.add(new NotEqualExpression());
    }


    @Override
    public Predicate createPredicate(CriteriaBuilder cb, Path<String> attributePath, FieldFilter fieldFilter) {
        switch (fieldFilter.getExpression().getType()) {
            case CONTAINS:
                String value = "%" + fieldFilter.getValue() + "%";
                return cb.like(attributePath, value);
            case EQUAL:
                return cb.equal(attributePath, fieldFilter.getValue());
            case NOT_EQUAL:
                return cb.notEqual(attributePath, fieldFilter.getValue());
            default:
                throw new RuntimeException("Expression " + fieldFilter.getExpression().getType() + " not allowed");
        }
    }
}