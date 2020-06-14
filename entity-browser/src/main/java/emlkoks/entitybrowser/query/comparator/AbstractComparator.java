package emlkoks.entitybrowser.query.comparator;

import emlkoks.entitybrowser.query.FieldFilter;
import emlkoks.entitybrowser.query.comparator.comparation.Comparation;
import emlkoks.entitybrowser.query.comparator.comparation.ComparationType;
import emlkoks.entitybrowser.query.comparator.comparation.IsNullComparation;
import emlkoks.entitybrowser.session.entity.FieldProperty;
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
    protected List<Comparation> expressions = new ArrayList<>();

    public AbstractComparator() {
        expressions.add(new IsNullComparation());
    }

    public Comparation[] getExpressions() {
        return expressions.toArray(new Comparation[]{});
    }

    public List<Node> createFilterRow(FieldProperty field) {
        ChoiceBox<Comparation> comparatorChoiceBox = createComparationTypeChoiceBox();
        Node valueField = createFieldValueField(field.getType());
        comparatorChoiceBox.valueProperty().addListener((observable, oldValue, newValue) ->
            valueField.setDisable(isNull(newValue.getType()) || isNotNull(newValue.getType()))
        );
        valueField.setDisable(isNull(comparatorChoiceBox.getValue().getType())
                || isNotNull(comparatorChoiceBox.getValue().getType()));
        return new ArrayList<>(Arrays.asList(new Label(field.getName()), comparatorChoiceBox, valueField));
    }

    private boolean isNull(ComparationType expressionType) {
        return ComparationType.IS_NULL.equals(expressionType);
    }

    private boolean isNotNull(ComparationType expressionType) {
        return ComparationType.IS_NOT_NULL.equals(expressionType);
    }


    private ChoiceBox<Comparation> createComparationTypeChoiceBox() {
        ChoiceBox<Comparation> expressionChoiceBox = new ChoiceBox<>();
        expressionChoiceBox.getItems().addAll(expressions);
        expressionChoiceBox.setValue(expressionChoiceBox.getItems().get(0));
        return expressionChoiceBox;
    }

    public abstract boolean canUseForClass(Class<?> clazz);

    protected abstract Node createFieldValueField(Class<?> clazz);

    public abstract Predicate createPredicate(CriteriaBuilder cb, Path<T> attributePath, FieldFilter fieldFilter);
}
