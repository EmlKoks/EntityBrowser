package emlkoks.entitybrowser.query.comparator;

import emlkoks.entitybrowser.query.FieldFilter;
import emlkoks.entitybrowser.query.comparator.expression.Expression;

import java.util.ArrayList;
import java.util.List;
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

    public abstract Predicate createPredicate(CriteriaBuilder cb, Path<T> attributePath, FieldFilter fieldFilter);
}
