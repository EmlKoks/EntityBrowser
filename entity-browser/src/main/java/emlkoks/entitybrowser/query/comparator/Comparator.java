package emlkoks.entitybrowser.query.comparator;

import emlkoks.entitybrowser.query.FieldFilter;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.Node;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import lombok.Getter;

/**
 * Created by EmlKoks on 15.06.19.
 */
public abstract class Comparator {
    @Getter
    protected List<ComparationType> comparationTypes = new ArrayList<>();

    public Comparator() {
        comparationTypes.add(ComparationType.IS_NULL);
    }

    public abstract boolean canUseForClass(Class<?> clazz);

    public abstract Node createFieldValueField(Class<?> clazz);

    public Predicate createPredicate(CriteriaBuilder cb, Path attributePath, FieldFilter fieldFilter) {
        switch (fieldFilter.getComparationType()) {
            case IS_NULL:
                return cb.isNull(attributePath);
            case IS_NOT_NULL:
                return cb.isNotNull(attributePath);
            default:
                return createCustomPredicate(cb, attributePath, fieldFilter);
        }
    }

    protected abstract Predicate createCustomPredicate(CriteriaBuilder cb, Path attributePath, FieldFilter fieldFilter);
}
