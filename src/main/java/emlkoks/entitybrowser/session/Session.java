package emlkoks.entitybrowser.session;

import emlkoks.entitybrowser.connection.Connection;
import emlkoks.entitybrowser.connection.Connector;
import emlkoks.entitybrowser.connection.Provider;
import emlkoks.entitybrowser.query.QueryBuilder;
import java.io.File;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.CriteriaBuilder;

/**
 * Created by EmlKoks on 09.04.17.
 */
public class Session {
    protected EntityList entityList;
    private Connection connection;
    private Provider provider;
    private EntityManager em;

    public Session(Connection connection, File entityJar, Provider provider) {
        this.connection = connection;
        entityList = new EntityList(entityJar);
        this.provider = provider;
    }

    public List<Class> getEntityClasses() {
        return entityList.getClasses();
    }

    public List<String> getClassNames() {
        return entityList.getClassNames();
    }

    public boolean connect() {
        EntityManagerFactory emf = Connector.createConnection(connection, entityList.getClasses(), provider);
        if (emf != null) {
            em = emf.createEntityManager();
            return true;
        } else {
            return false;
        }
    }

    public Entity getEntity(String entityName) {
        return entityList.getEntity(entityName);
    }

    public List<Object> find(QueryBuilder pc) {
        return em.createQuery(pc.getCriteriaQuery())
                .setMaxResults(100)
                .getResultList();
    }

    public CriteriaBuilder getCriteriaBuilder() {
        return em.getCriteriaBuilder();
    }
}
