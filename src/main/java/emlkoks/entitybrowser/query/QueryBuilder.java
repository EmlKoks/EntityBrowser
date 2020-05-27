package emlkoks.entitybrowser.query;

import emlkoks.entitybrowser.entity.EntityDetails;
import emlkoks.entitybrowser.query.comparator.AbstractComparator;
import emlkoks.entitybrowser.query.comparator.ComparatorFactory;
import emlkoks.entitybrowser.entity.FieldProperty;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by EmlKoks on 17.04.17.
 */
@Slf4j
public class QueryBuilder {
    private Root root;
    private CriteriaBuilder cb;
    private CriteriaQuery cq;
    private List<Predicate> predicates;

    public QueryBuilder(CriteriaBuilder cb, EntityDetails entity, Collection<FieldFilter> fieldFilters) {
        predicates = new ArrayList<>();
        this.cb = cb;
        cq = cb.createQuery(entity.getClazz());
        root = cq.from(entity.getClazz());
        build(fieldFilters);
    }

    protected QueryBuilder() {
    }

    private void build(Collection<FieldFilter> fieldFilters) {
        fieldFilters.forEach(this::createPredicate);
    }

    private void createPredicate(FieldFilter fieldFilter) {
        if (fieldFilter.getFieldProperty().getField().getAnnotation(OneToMany.class) != null) {
            log.debug("OneToMany");
        }
        if (fieldFilter.getFieldProperty().getField().getAnnotation(ManyToOne.class) != null) {
            log.debug("ManyToOne");
        }
        if (fieldFilter.getFieldProperty().getField().getAnnotation(ManyToMany.class) != null) {
            log.debug("ManyToMany");
        }
        AbstractComparator comparator = new ComparatorFactory().getComparator(fieldFilter.getFieldProperty());
        predicates.add(comparator.createPredicate(cb, getPath(fieldFilter.getFieldProperty()), fieldFilter));
    }

    private Path getPath(FieldProperty fieldProperty) {
        return root.get(root.getModel().getDeclaredSingularAttribute(fieldProperty.getName()));
    }


    public CriteriaQuery getCriteriaQuery() {
        addPredicatesToCq();
        return cq;
    }

    private void addPredicatesToCq() {
        for (Predicate p : predicates) {
            cq.where(p);
        }
    }
}
