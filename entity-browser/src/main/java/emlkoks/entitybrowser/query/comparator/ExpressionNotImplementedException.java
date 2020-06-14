package emlkoks.entitybrowser.query.comparator;

import emlkoks.entitybrowser.query.comparator.comparation.Comparation;

public class ExpressionNotImplementedException extends RuntimeException {

    public ExpressionNotImplementedException(Comparation expression) {
        super("Expression " + expression.getType() + " not allowed");
    }
}
