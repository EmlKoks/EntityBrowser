package emlkoks.entitybrowser.query.comparator;

import emlkoks.entitybrowser.query.FieldFilter;
import emlkoks.entitybrowser.query.comparator.expression.Expression;
import emlkoks.entitybrowser.query.comparator.expression.ExpressionType;
import emlkoks.entitybrowser.query.comparator.expression.IsNullExpression;
import emlkoks.entitybrowser.session.FieldProperty;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;

/**
 * Created by EmlKoks on 15.06.19.
 */
public abstract class AbstractComparator<T> {
    List<Expression> expressions = new ArrayList<>();

    public AbstractComparator() {
        expressions.add(new IsNullExpression());
    }

    Expression[] getExpressions() {
        return expressions.toArray(new Expression[]{});
    }

    public List<Node> createFilterRow(FieldProperty field) {
        ChoiceBox<Expression> comparatorChoiceBox = createComparationTypeChoiceBox();
        Node valueField = createFieldValueField(field.getField().getType());
        comparatorChoiceBox.valueProperty().addListener((observable, oldValue, newValue) ->
            valueField.setDisable(isNull(newValue.getType()) || isNotNull(newValue.getType()))
        );
        valueField.setDisable(isNull(comparatorChoiceBox.getValue().getType())
                || isNotNull(comparatorChoiceBox.getValue().getType()));
        return new ArrayList<>(Arrays.asList(new Label(field.getName()), comparatorChoiceBox, valueField));
    }

    private boolean isNull(ExpressionType expressionType) {
        return ExpressionType.IS_NULL.equals(expressionType);
    }

    private boolean isNotNull(ExpressionType expressionType) {
        return ExpressionType.IS_NOT_NULL.equals(expressionType);
    }


    private ChoiceBox<Expression> createComparationTypeChoiceBox() {
        ChoiceBox<Expression> expressionChoiceBox = new ChoiceBox<>();
        expressionChoiceBox.getItems().addAll(expressions);
        expressionChoiceBox.setValue(expressionChoiceBox.getItems().get(0));
        return expressionChoiceBox;
    }

    abstract Node createFieldValueField(Class clazz);

    public abstract Predicate createPredicate(CriteriaBuilder cb, Path<T> attributePath, FieldFilter fieldFilter);
}
