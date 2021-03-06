package emlkoks.entitybrowser.query.comparator;

import emlkoks.entitybrowser.Main;
import emlkoks.entitybrowser.query.FieldFilter;
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
public class BooleanComparator extends Comparator {

    BooleanComparator() {
        comparationTypes.add(ComparationType.EQUAL);
        comparationTypes.add(ComparationType.NOT_EQUAL);
    }

    @Override
    public boolean canUseForClass(Class<?> clazz) {
        return clazz == boolean.class || Boolean.class.isAssignableFrom(clazz);//TODO add bit
    }

    @Override
    public Node createFieldValueField(Class<?> clazz) {
        ToggleGroup toggleGroup = new ToggleGroup();
        RadioButton trueButton = new RadioButton(Main.resources.getString("boolean.true"));
        trueButton.setToggleGroup(toggleGroup);
        trueButton.setSelected(true);
        RadioButton falseButton = new RadioButton(Main.resources.getString("boolean.false"));
        falseButton.setToggleGroup(toggleGroup);
        return new HBox(trueButton, falseButton);
    }

    @Override
    protected Predicate createCustomPredicate(CriteriaBuilder cb, Path attributePath, FieldFilter fieldFilter) {
        switch (fieldFilter.getComparationType()) {
            case EQUAL:
                return cb.equal(attributePath, fieldFilter.getValue());
            case NOT_EQUAL:
                return cb.notEqual(attributePath, fieldFilter.getValue());
            default:
                throw new ComparationTypeNotAllowedException(fieldFilter.getComparationType(), this);
        }
    }
}
