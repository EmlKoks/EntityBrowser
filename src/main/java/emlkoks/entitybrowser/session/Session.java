package emlkoks.entitybrowser.session;

import emlkoks.entitybrowser.connection.Connection;
import emlkoks.entitybrowser.connection.Connector;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.File;
import java.util.List;

/**
 * Created by EmlKoks on 09.04.17.
 */
public class Session {
    private EntityList entityList;
    private Connection connection;
    private EntityManager em;

    public Session(Connection connection, File entityJar) {
        this.connection = connection;
        entityList = new EntityList();
        entityList.loadEntities(entityJar);
    }

    public List<Class> getEntityClasses(){
        return entityList.getClasses();
    }

    public List<String> getClassNames(){
        return entityList.getClassNames();
    }

    public boolean connect(){
        EntityManagerFactory emf = Connector.createConnection(connection, entityList.getClasses());
        if(emf != null) {
            em = emf.createEntityManager();
            return true;
        }
        else
            return false;
    }

    public Entity getEntity(String entityName){
        return entityList.getEntity(entityName);
    }

    public List<Object> find(String entityName, List<Predicate> predicates){
        Entity entity = entityList.getEntity(entityName);
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery(entity.getClass());
        Root root = cq.from(entity.getClazz());
        cq.select(root);
        if(predicates!= null){
            for(Predicate p : predicates)
                cq.where(p);
        }
        return em.createQuery(cq).setMaxResults(100).getResultList();
    }

    public CriteriaBuilder getCriteriaBuilder(){
        return em.getCriteriaBuilder();
    }
}
