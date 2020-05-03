package emlkoks.entitybrowser.query.comparator;

import emlkoks.entitybrowser.query.FieldFilter;
import emlkoks.entitybrowser.query.comparator.expression.EqualExpression;
import emlkoks.entitybrowser.query.comparator.expression.LessExpression;
import emlkoks.entitybrowser.query.comparator.expression.LessOrEqualExpression;
import emlkoks.entitybrowser.query.comparator.expression.MoreExpression;
import emlkoks.entitybrowser.query.comparator.expression.MoreOrEqualExpression;
import emlkoks.entitybrowser.query.comparator.expression.NotEqualExpression;
import emlkoks.entitybrowser.view.control.field.NumberTextField;
import javafx.scene.Node;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;

/**
 * Created by EmlKoks on 15.06.19.
 */
public class NumberComparator extends AbstractComparator<Number> {

    NumberComparator() {
        expressions.add(new EqualExpression());
        expressions.add(new NotEqualExpression());
        expressions.add(new MoreExpression());
        expressions.add(new MoreOrEqualExpression());
        expressions.add(new LessExpression());
        expressions.add(new LessOrEqualExpression());
//        expressions.add(new BetweenExpression());//Remove?
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
    Node createFieldValueField(Class<?> clazz) {
        return new NumberTextField(clazz);
    }

    @Override
    public Predicate createPredicate(CriteriaBuilder cb, Path attributePath, FieldFilter fieldFilter) {
        switch (fieldFilter.getExpression().getType()) {
            case EQUAL:
                return cb.equal(attributePath, fieldFilter.getValue());
            case NOT_EQUAL:
                return cb.notEqual(attributePath, fieldFilter.getValue());
            default://TODO implement rest expressions
                throw new ExpressionNotImplementedException(fieldFilter.getExpression());
        }
    }

}
