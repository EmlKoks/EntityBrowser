package emlkoks.entitybrowser.connection.provider;

import emlkoks.entitybrowser.connection.Connection;
import emlkoks.entitybrowser.connection.Property;
import emlkoks.entitybrowser.session.entity.EntityList;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.hibernate.jpa.AvailableSettings;


public class HibernateProvider extends JpaProvider {

    public HibernateProvider(Connection connection) {
        super(connection);
    }

    @Override
    public EntityManagerFactory createEntityManagerFactory(EntityList entityList) {
        properties.add(new Property(AvailableSettings.LOADED_CLASSES, entityList.getClasses()));
        return entityManagerFactory = Persistence.createEntityManagerFactory("Hibernate", getMapProperties());
    }
}
