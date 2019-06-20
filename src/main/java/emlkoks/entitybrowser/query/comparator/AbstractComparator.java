package emlkoks.entitybrowser.query.comparator;

import emlkoks.entitybrowser.query.FieldFilter;
import emlkoks.entitybrowser.query.comparator.expression.Expression;
import emlkoks.entitybrowser.session.FieldProperty;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;

/**
 * Created by EmlKoks on 15.06.19.
 */
public abstract class AbstractComparator<T> {
    List<Expression> expressions = new ArrayList<>();

    Expression[] getExpressions() {
        return expressions.toArray(new Expression[]{});
    }

    public Node[] createFilterRow(FieldProperty field) {
        List<Node> rowNodes = new ArrayList<>();
        rowNodes.add(new Label(field.getName()));
        rowNodes.add(createChoiceBox());
        rowNodes.addAll(createValueNodes());
        return rowNodes.toArray(new Node[]{});
    }

    private ChoiceBox<Expression> createChoiceBox() {
        ChoiceBox<Expression> expressionChoiceBox = new ChoiceBox<>();
        expressionChoiceBox.getItems().addAll(expressions);
        expressionChoiceBox.setValue(expressionChoiceBox.getItems().get(0));
        return expressionChoiceBox;
    }

    protected List<Node> createValueNodes() {
        TextField value = new TextField();
        return Arrays.asList(value);
    }

    public abstract Predicate createPredicate(CriteriaBuilder cb, Path<T> attributePath, FieldFilter fieldFilter);
}
