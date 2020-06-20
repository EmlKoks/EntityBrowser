package emlkoks.entitybrowser.query.comparator;

import emlkoks.entitybrowser.query.FieldFilter;
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
public abstract class Comparator {
    protected List<ComparationType> comparationTypes = new ArrayList<>();

    public Comparator() {
        comparationTypes.add(ComparationType.IS_NULL);
    }

    public List<Node> createFilterRow(FieldProperty field) {
        ChoiceBox<ComparationType> comparatorChoiceBox = createComparationTypeChoiceBox();
        Node valueField = createFieldValueField(field.getType());
        comparatorChoiceBox.valueProperty().addListener((observable, oldValue, newValue) ->
            valueField.setDisable(isNull(newValue) || isNotNull(newValue))
        );
        valueField.setDisable(isNull(comparatorChoiceBox.getValue())
                || isNotNull(comparatorChoiceBox.getValue()));
        return Arrays.asList(new Label(field.getName()), comparatorChoiceBox, valueField);
    }

    private boolean isNull(ComparationType comparationType) {
        return ComparationType.IS_NULL.equals(comparationType);
    }

    private boolean isNotNull(ComparationType expressionType) {
        return ComparationType.IS_NOT_NULL.equals(expressionType);
    }


    private ChoiceBox<ComparationType> createComparationTypeChoiceBox() {
        ChoiceBox<ComparationType> expressionChoiceBox = new ChoiceBox<>();
        expressionChoiceBox.getItems().addAll(comparationTypes);
        expressionChoiceBox.setValue(expressionChoiceBox.getItems().get(0));
        return expressionChoiceBox;
    }

    public abstract boolean canUseForClass(Class<?> clazz);

    protected abstract Node createFieldValueField(Class<?> clazz);

    public Predicate createPredicate(CriteriaBuilder cb, Path attributePath, FieldFilter fieldFilter) {
        switch (fieldFilter.getComparationType()) {
            case IS_NULL:
                return cb.isNull(attributePath);
            case IS_NOT_NULL:
                return cb.isNotNull(attributePath);
            default:
                return createCustomPredicate(cb, attributePath, fieldFilter);
        }
    };

    protected abstract Predicate createCustomPredicate(CriteriaBuilder cb, Path attributePath, FieldFilter fieldFilter);
}
