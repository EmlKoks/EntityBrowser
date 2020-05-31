package emlkoks.entitybrowser.query.comparator.expression;

import emlkoks.entitybrowser.Main;

/**
 * Created by EmlKoks on 11.06.19.
 */
public abstract class Expression {
    private ExpressionType expressionType;
    private String expressionLabel;

    Expression(ExpressionType expressionType) {
        this.expressionType = expressionType;                       //TODO depends by comparator type
        this.expressionLabel = Main.resources.getString("expression." + expressionType.name().toLowerCase());
    }

    public String toString() {
        return expressionLabel;
    }

    public ExpressionType getType() {
        return expressionType;
    }
}
