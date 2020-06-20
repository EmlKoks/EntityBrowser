package emlkoks.entitybrowser.query.comparator;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;

public class UtilClass {

    public static Path buildPath(CriteriaBuilder criteriaBuilder, Class clazz, String fieldName) {
        var criteriaQuery = criteriaBuilder.createQuery(clazz);
        var root = criteriaQuery.from(clazz);
        return root.get(fieldName);
    }
}
