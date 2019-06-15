package emlkoks.entitybrowser.query.comparator;

import emlkoks.entitybrowser.query.comparator.expression.*;

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
