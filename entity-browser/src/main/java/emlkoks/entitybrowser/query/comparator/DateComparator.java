package emlkoks.entitybrowser.query.comparator;

import emlkoks.entitybrowser.query.FieldFilter;
import emlkoks.entitybrowser.query.comparator.comparation.BetweenComparation;
import emlkoks.entitybrowser.query.comparator.comparation.EqualComparation;
import emlkoks.entitybrowser.query.comparator.comparation.LessComparation;
import emlkoks.entitybrowser.query.comparator.comparation.LessOrEqualComparation;
import emlkoks.entitybrowser.query.comparator.comparation.MoreComparation;
import emlkoks.entitybrowser.query.comparator.comparation.MoreOrEqualComparation;
import emlkoks.entitybrowser.query.comparator.comparation.NotEqualComparation;
import java.util.Date;
import javafx.scene.Node;
import javafx.scene.control.DatePicker;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;

/**
 * Created by EmlKoks on 15.06.19.
 */
public class DateComparator extends AbstractComparator<Date> {

    DateComparator() {
        expressions.add(new EqualComparation());
        expressions.add(new NotEqualComparation());
        expressions.add(new MoreComparation());
        expressions.add(new MoreOrEqualComparation());
        expressions.add(new LessComparation());
        expressions.add(new LessOrEqualComparation());
        expressions.add(new BetweenComparation());
    }

    @Override
    public boolean canUseForClass(Class<?> clazz) {
        return Date.class.isAssignableFrom(clazz);
    }

    @Override
    protected Node createFieldValueField(Class<?> clazz) {
        return new DatePicker();
    }

    @Override
    public Predicate createPredicate(CriteriaBuilder cb, Path attributePath, FieldFilter fieldFilter) {
        return null;//TODO
    }
}
