package emlkoks.entitybrowser.query;

import emlkoks.entitybrowser.session.FieldProperty;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by EmlKoks on 17.04.17.
 */
@Slf4j
public class QueryCreator {
    private Root root;
    private CriteriaBuilder cb;
    private CriteriaQuery cq;
    private Class clazz;
    private List<Predicate> predicates;

    public QueryCreator(Class clazz, CriteriaBuilder cb) {
        predicates = new ArrayList<>();
        this.clazz = clazz;
        this.cb = cb;
        cq = cb.createQuery(clazz);
        root = cq.from(clazz);
    }

    public void createPredicate(FieldProperty fp, String expression, String value) {
        Predicate p = null;
        if (fp.getField().getAnnotation(OneToMany.class) != null) {
            log.debug("OneToMany");
        }
        if (fp.getField().getAnnotation(ManyToOne.class) != null) {
            log.debug("ManyToOne");
        }
        if (fp.getField().getAnnotation(ManyToMany.class) != null) {
            log.debug("ManyToMany");
        }
        if (fp.getField().getType() == String.class) {
            p = createStringPredicate(fp, expression, value);
        }
        if (p != null) {
            predicates.add(p);
        }
    }

    private Predicate createStringPredicate(FieldProperty fp, String expression, String value) {
        if ("Zawiera".equals(expression)) {
            return cb.like(root.get(root.getModel().getDeclaredSingularAttribute(fp.getName())), "%" + value + "%");
        } else if ("Równe".equals(expression)) {
            return cb.equal(root.get(root.getModel().getDeclaredSingularAttribute(fp.getName())), value);
        } else {
            return null;
        }
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
