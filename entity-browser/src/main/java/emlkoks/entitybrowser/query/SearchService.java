package emlkoks.entitybrowser.query;

import emlkoks.entitybrowser.connection.provider.JpaProvider;
import emlkoks.entitybrowser.session.SearchResults;
import emlkoks.entitybrowser.session.entity.ClassDetails;

import java.util.List;
import javax.persistence.EntityManager;

public class SearchService {
    private JpaProvider provider;

    public SearchService(JpaProvider provider) {
        this.provider = provider;
    }

    public SearchResults search(ClassDetails entity, List<FieldFilter> filters) {
        QueryBuilder queryBuilder = new QueryBuilder(provider.getCriteriaBuilder(), entity, filters);
        return new SearchResults(entity, find(provider.getEntityManager(), queryBuilder));
    }

    private List find(EntityManager entityManager, QueryBuilder queryBuilder) {
        return entityManager.createQuery(queryBuilder.getCriteriaQuery())
                .setMaxResults(100)
                .getResultList();
    }

}
