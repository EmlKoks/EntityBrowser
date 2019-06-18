package emlkoks.entitybrowser.query.comparator;

import emlkoks.entitybrowser.query.FieldFilter;
import emlkoks.entitybrowser.query.comparator.expression.BetweenExpression;
import emlkoks.entitybrowser.query.comparator.expression.EqualExpression;
import emlkoks.entitybrowser.query.comparator.expression.LessExpression;
import emlkoks.entitybrowser.query.comparator.expression.LessOrEqualExpression;
import emlkoks.entitybrowser.query.comparator.expression.MoreExpression;
import emlkoks.entitybrowser.query.comparator.expression.MoreOrEqualExpression;
import emlkoks.entitybrowser.query.comparator.expression.NotEqualExpression;

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
        expressions.add(new BetweenExpression());
    }

    @Override
    public Predicate createPredicate(CriteriaBuilder cb, Path attributePath, FieldFilter fieldFilter) {
        return null;//TODO
    }
}
