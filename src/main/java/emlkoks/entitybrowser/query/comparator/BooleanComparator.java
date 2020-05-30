package emlkoks.entitybrowser.query.comparator;

import emlkoks.entitybrowser.Main;
import emlkoks.entitybrowser.query.FieldFilter;
import emlkoks.entitybrowser.query.comparator.expression.EqualExpression;
import emlkoks.entitybrowser.query.comparator.expression.NotEqualExpression;
import javafx.scene.Node;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;

/**
 * Created by EmlKoks on 15.06.19.
 */
public class BooleanComparator extends AbstractComparator<Boolean> {

    BooleanComparator() {
        expressions.add(new EqualExpression());
        expressions.add(new NotEqualExpression());
    }

    @Override
    public boolean canUseForClass(Class<?> clazz) {
        return clazz == boolean.class || Boolean.class.isAssignableFrom(clazz);
    }

    @Override
    protected Node createFieldValueField(Class<?> clazz) {
        ToggleGroup toggleGroup = new ToggleGroup();
        RadioButton trueButton = new RadioButton(Main.resources.getString("boolean.true"));
        trueButton.setToggleGroup(toggleGroup);
        trueButton.setSelected(true);
        RadioButton falseButton = new RadioButton(Main.resources.getString("boolean.false"));
        falseButton.setToggleGroup(toggleGroup);
        return new HBox(trueButton, falseButton);
    }

    @Override
    public Predicate createPredicate(CriteriaBuilder cb, Path attributePath, FieldFilter fieldFilter) {
        return null;//TODO
    }
}
