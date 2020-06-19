package emlkoks.entitybrowser.query.comparator;

import emlkoks.entitybrowser.query.FieldFilter;
import emlkoks.entitybrowser.view.control.field.NumberTextField;
import javafx.scene.Node;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;

/**
 * Created by EmlKoks on 15.06.19.
 */
public class NumberComparator extends Comparator {

    NumberComparator() {
        comparationTypes.add(ComparationType.EQUAL);
        comparationTypes.add(ComparationType.NOT_EQUAL);
        comparationTypes.add(ComparationType.MORE);
        comparationTypes.add(ComparationType.MORE_OR_EQUAL);
        comparationTypes.add(ComparationType.LESS);
        comparationTypes.add(ComparationType.LESS_OR_EQUAL);
//        expressions.add(new BetweenExpression());//Remove?
    }

    @Override
    public boolean canUseForClass(Class<?> clazz) {
        return clazz == int.class
                || clazz == float.class
                || clazz == long.class
                || clazz == double.class
                || clazz == short.class
                || Number.class.isAssignableFrom(clazz);
    }

    @Override
    protected Node createFieldValueField(Class<?> clazz) {
        return new NumberTextField(clazz);
    }

    @Override
    public Predicate createPredicate(CriteriaBuilder cb, Path attributePath, FieldFilter fieldFilter) {
        switch (fieldFilter.getComparationType()) {
            case EQUAL:
                return cb.equal(attributePath, fieldFilter.getValue());
            case NOT_EQUAL:
                return cb.notEqual(attributePath, fieldFilter.getValue());
            default://TODO implement rest expressions
                throw new ComparationTypeNotImplementedException(fieldFilter.getComparationType());
        }
    }

}
