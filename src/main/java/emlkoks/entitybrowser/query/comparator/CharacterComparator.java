package emlkoks.entitybrowser.query.comparator;

import emlkoks.entitybrowser.query.FieldFilter;
import emlkoks.entitybrowser.query.comparator.expression.EqualExpression;
import emlkoks.entitybrowser.query.comparator.expression.NotEqualExpression;
import javafx.scene.Node;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;

/**
 * Created by EmlKoks on 15.06.19.
 */
public class CharacterComparator extends AbstractComparator<Character> {

    CharacterComparator() {
        expressions.add(new EqualExpression());
        expressions.add(new NotEqualExpression());
    }

    @Override
    Node createFieldValueField(Class clazz) {
        return null;//TODO
    }

    @Override
    public Predicate createPredicate(CriteriaBuilder cb, Path attributePath, FieldFilter fieldFilter) {
        switch (fieldFilter.getExpression().getType()) {
            case EQUAL:
                return cb.equal(attributePath, fieldFilter.getValue());
            case NOT_EQUAL:
                return cb.notEqual(attributePath, fieldFilter.getValue());
            default:
                throw new ExpressionNotImplementedException(fieldFilter.getExpression());
        }
    }
}
