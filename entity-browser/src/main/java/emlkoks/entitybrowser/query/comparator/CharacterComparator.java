package emlkoks.entitybrowser.query.comparator;

import emlkoks.entitybrowser.query.FieldFilter;
import emlkoks.entitybrowser.query.comparator.expression.EqualComparation;
import emlkoks.entitybrowser.query.comparator.expression.NotEqualComparation;
import javafx.scene.Node;
import javafx.scene.control.TextField;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;

/**
 * Created by EmlKoks on 15.06.19.
 */
public class CharacterComparator extends AbstractComparator<Character> {

    CharacterComparator() {
        expressions.add(new EqualComparation());
        expressions.add(new NotEqualComparation());
    }

    @Override
    public boolean canUseForClass(Class<?> clazz) {
        return clazz == char.class || Character.class.isAssignableFrom(clazz);
    }

    @Override
    protected Node createFieldValueField(Class<?> clazz) {
        return new TextField();
    }

    @Override
    public Predicate createPredicate(CriteriaBuilder cb, Path attributePath, FieldFilter fieldFilter) {
        switch (fieldFilter.getComparation().getType()) {
            case EQUAL:
                return cb.equal(attributePath, fieldFilter.getValue());
            case NOT_EQUAL:
                return cb.notEqual(attributePath, fieldFilter.getValue());
            default:
                throw new ExpressionNotImplementedException(fieldFilter.getComparation());
        }
    }
}
