package emlkoks.entitybrowser;

import emlkoks.entitybrowser.session.FieldProperty;

import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * Created by EmlKoks on 17.04.17.
 */
public class PredicateCreator {

    public static Predicate createPredicate(CriteriaBuilder cb, FieldProperty fp, String expression, String value){
        if(fp.getField().getAnnotation(OneToMany.class) != null) System.out.println("OneToMany");
        if(fp.getField().getAnnotation(ManyToOne.class) != null) System.out.println("ManyToOne");
        if(fp.getField().getAnnotation(ManyToMany.class) != null) System.out.println("ManyToMany");
        if(fp.getField().getType() == String.class){
            return createStringPredicate(cb, fp, expression, value);
        }
        return null;
    }

    private static Predicate createStringPredicate(CriteriaBuilder cb, FieldProperty fp, String expression, String value){
        CriteriaQuery cq = cb.createQuery(fp.getParentClass());
        Root root = cq.from(fp.getParentClass());
        Predicate p = cb.equal(root.get(root.getModel().getDeclaredSingularAttribute(fp.getName())), value);
        return p;
    }
}
