package emlkoks.entitybrowser.connection.factoryCreator;

import emlkoks.entitybrowser.connection.Connection;
import org.hibernate.jpa.AvailableSettings;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

/**
 * Created by EmlKoks on 10.03.17.
 */
public class HibernateEntityManagerFactory extends EntityManagerFactoryCreator {

    public HibernateEntityManagerFactory(Connection connection, List<Class> classList){
        super(connection);
        properties.put(AvailableSettings.LOADED_CLASSES, classList);
    }

    @Override
    public EntityManagerFactory createEntityManagerFactory() {
        return Persistence.createEntityManagerFactory("Hibernate", properties);
    }
}
