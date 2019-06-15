package emlkoks.entitybrowser.query.comparator;

import emlkoks.entitybrowser.query.comparator.expression.BetweenExpression;
import emlkoks.entitybrowser.query.comparator.expression.EqualExpression;
import emlkoks.entitybrowser.query.comparator.expression.LessExpression;
import emlkoks.entitybrowser.query.comparator.expression.LessOrEqualExpression;
import emlkoks.entitybrowser.query.comparator.expression.MoreExpression;
import emlkoks.entitybrowser.query.comparator.expression.MoreOrEqualExpression;
import emlkoks.entitybrowser.query.comparator.expression.NotEqualExpression;

/**
 * Created by EmlKoks on 15.06.19.
 */
public class NumberComparator extends AbstractComparator {

    public NumberComparator() {
        expressions.add(new EqualExpression());
        expressions.add(new NotEqualExpression());
        expressions.add(new MoreExpression());
        expressions.add(new MoreOrEqualExpression());
        expressions.add(new LessExpression());
        expressions.add(new LessOrEqualExpression());
        expressions.add(new BetweenExpression());
    }
}
