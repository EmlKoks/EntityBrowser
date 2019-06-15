package emlkoks.entitybrowser.query.comparator;

import emlkoks.entitybrowser.query.comparator.expression.*;

/**
 * Created by EmlKoks on 15.06.19.
 */
public class BooleanComparator extends AbstractComparator {

    public BooleanComparator() {
        expressions.add(new EqualExpression());
        expressions.add(new NotEqualExpression());
    }
}
