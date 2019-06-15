package emlkoks.entitybrowser.query.comparator;

import emlkoks.entitybrowser.query.comparator.expression.ContainsExpression;
import emlkoks.entitybrowser.query.comparator.expression.EqualExpression;
import emlkoks.entitybrowser.query.comparator.expression.NotEqualExpression;

/**
 * Created by EmlKoks on 11.06.19.
 */
public class StringComparator extends AbstractComparator {

    public StringComparator() {
        expressions.add(new ContainsExpression());
        expressions.add(new EqualExpression());
        expressions.add(new NotEqualExpression());
    }
}
