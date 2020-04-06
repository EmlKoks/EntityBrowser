package emlkoks.entitybrowser.query;

import emlkoks.entitybrowser.session.Entity;
import emlkoks.entitybrowser.session.Session;

import java.util.List;
import javax.persistence.EntityManager;

public class SearchService {

    public static List searchResults(Session session, Entity entity, List<FieldFilter> filters) {
        QueryBuilder queryBuilder = new QueryBuilder(session.getCriteriaBuilder(), entity, filters);
        return find(session.getEntityManager(), queryBuilder);
    }

    private static List find(EntityManager entityManager, QueryBuilder queryBuilder) {
//        queryBuilder.build();
        return entityManager.createQuery(queryBuilder.getCriteriaQuery())
                .setMaxResults(100)
                .getResultList();
    }
}
