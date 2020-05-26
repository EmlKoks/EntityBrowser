package emlkoks.entitybrowser.query;

import emlkoks.entitybrowser.entity.EntityDetails;
import emlkoks.entitybrowser.session.SearchResults;
import emlkoks.entitybrowser.session.Session;

import java.util.List;
import javax.persistence.EntityManager;

public class SearchService {
    private Session session;

    public SearchService(Session session) {
        this.session = session;
    }

    public SearchResults search(EntityDetails entity, List<FieldFilter> filters) {
        QueryBuilder queryBuilder = new QueryBuilder(session.getCriteriaBuilder(), entity, filters);
        return new SearchResults(entity, find(session.getEntityManager(), queryBuilder));
    }

    private List find(EntityManager entityManager, QueryBuilder queryBuilder) {
        return entityManager.createQuery(queryBuilder.getCriteriaQuery())
                .setMaxResults(100)
                .getResultList();
    }

}
