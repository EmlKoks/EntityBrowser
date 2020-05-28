package emlkoks.entitybrowser.session;

import emlkoks.entitybrowser.connection.Connection;
import emlkoks.entitybrowser.connection.ConnectionHelper;
import emlkoks.entitybrowser.session.entity.ClassDetails;
import emlkoks.entitybrowser.session.entity.EntityList;

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
    private EntityManager entityManager;

    public Session(Connection connection) {
        this.connection = connection;
        File entityLibrary = new File(connection.getLibraryPath());
        if (!entityLibrary.exists()) {
            throw new RuntimeException();
        }
        entityList = new EntityList(entityLibrary);
    }

    public List<String> getClassNames() {
        return entityList.getClassNames();
    }

    public boolean connect() {
        EntityManagerFactory emf = ConnectionHelper.createConnection(connection, entityList.getClasses());
        if (emf != null) {
            entityManager = emf.createEntityManager();
            return true;
        } else {
            return false;
        }
    }

    public ClassDetails getEntity(String entityName) {
        return entityList.getEntity(entityName);
    }

    public CriteriaBuilder getCriteriaBuilder() {
        return entityManager.getCriteriaBuilder();
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }
}
