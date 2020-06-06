package emlkoks.entitybrowser.connection.provider;

import emlkoks.entitybrowser.connection.Connection;
import emlkoks.entitybrowser.connection.Property;
import emlkoks.entitybrowser.connection.Provider;
import emlkoks.entitybrowser.session.entity.EntityList;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.jpa.AvailableSettings;

@Slf4j
public class HibernateProvider extends JpaProvider {

    public HibernateProvider(Connection connection) {
        super(connection);
    }

    @Override
    protected EntityManagerFactory createEntityManagerFactory(EntityList entityList) {
        if (entityList.hasClasses()) {
            properties.add(new Property(AvailableSettings.LOADED_CLASSES, entityList.getClasses()));
        }
        try {
            return entityManagerFactory =
                    Persistence.createEntityManagerFactory(Provider.Hibernate.getUnitName(), getMapProperties());
        } catch (PersistenceException exception) {
            log.warn("Cannot EntityManagerFactory", exception);
            throw new ProviderException("Cannot EntityManagerFactory", exception);
        }
    }
}
