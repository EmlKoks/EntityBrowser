package emlkoks.entitybrowser.query.comparator;

import emlkoks.entitybrowser.query.FieldFilter;
import emlkoks.entitybrowser.query.comparator.comparation.EqualComparation;
import emlkoks.entitybrowser.query.comparator.comparation.LessComparation;
import emlkoks.entitybrowser.query.comparator.comparation.LessOrEqualComparation;
import emlkoks.entitybrowser.query.comparator.comparation.MoreComparation;
import emlkoks.entitybrowser.query.comparator.comparation.MoreOrEqualComparation;
import emlkoks.entitybrowser.query.comparator.comparation.NotEqualComparation;
import emlkoks.entitybrowser.view.control.field.NumberTextField;
import javafx.scene.Node;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;

/**
 * Created by EmlKoks on 15.06.19.
 */
public class NumberComparator extends AbstractComparator<Number> {

    NumberComparator() {
        expressions.add(new EqualComparation());
        expressions.add(new NotEqualComparation());
        expressions.add(new MoreComparation());
        expressions.add(new MoreOrEqualComparation());
        expressions.add(new LessComparation());
        expressions.add(new LessOrEqualComparation());
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
        switch (fieldFilter.getComparation().getType()) {
            case EQUAL:
                return cb.equal(attributePath, fieldFilter.getValue());
            case NOT_EQUAL:
                return cb.notEqual(attributePath, fieldFilter.getValue());
            default://TODO implement rest expressions
                throw new ExpressionNotImplementedException(fieldFilter.getComparation());
        }
    }

}
