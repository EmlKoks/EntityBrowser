package emlkoks.entitybrowser.query.comparator;

import emlkoks.entitybrowser.query.comparator.expression.Expression;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by EmlKoks on 15.06.19.
 */
public abstract class AbstractComparator {
    protected List<Expression> expressions = new ArrayList<>();

    public Expression[] getExpressions() {
        return expressions.toArray(new Expression[]{});
    }
}
