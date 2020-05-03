package emlkoks.entitybrowser.query.comparator;

import emlkoks.entitybrowser.query.comparator.expression.Expression;

public class ExpressionNotImplementedException extends RuntimeException {

    public ExpressionNotImplementedException(Expression expression) {
        super("Expression " + expression.getType() + " not allowed");
    }
}
