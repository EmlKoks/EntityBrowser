package emlkoks.entitybrowser.session;

import emlkoks.entitybrowser.connection.Connection;
import emlkoks.entitybrowser.connection.ConnectionHelper;
import emlkoks.entitybrowser.connection.Provider;

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
    private EntityManager entityManager;

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
        EntityManagerFactory emf = ConnectionHelper.createConnection(connection, entityList.getClasses(), provider);
        if (emf != null) {
            entityManager = emf.createEntityManager();
            return true;
        } else {
            return false;
        }
    }

    public Entity getEntity(String entityName) {
        return entityList.getEntity(entityName);
    }

    public CriteriaBuilder getCriteriaBuilder() {
        return entityManager.getCriteriaBuilder();
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }
}
