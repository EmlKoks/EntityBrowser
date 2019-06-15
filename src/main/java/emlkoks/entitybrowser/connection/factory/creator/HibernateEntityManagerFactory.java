package emlkoks.entitybrowser.connection.factory.creator;

import emlkoks.entitybrowser.connection.Connection;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.hibernate.jpa.AvailableSettings;

/**
 * Created by EmlKoks on 10.03.17.
 */
public class HibernateEntityManagerFactory extends EntityManagerFactoryCreator {

    public HibernateEntityManagerFactory(Connection connection, List<Class> classList) {
        super(connection);
        properties.put(AvailableSettings.LOADED_CLASSES, classList);
    }

    @Override
    public EntityManagerFactory createEntityManagerFactory() {
        return Persistence.createEntityManagerFactory("Hibernate", properties);
    }
}
