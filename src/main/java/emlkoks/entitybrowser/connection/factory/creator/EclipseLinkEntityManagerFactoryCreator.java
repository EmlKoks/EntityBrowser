package emlkoks.entitybrowser.connection.factory.creator;

import emlkoks.entitybrowser.connection.Connection;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Created by EmlKoks on 10.03.17.
 */
public class EclipseLinkEntityManagerFactoryCreator extends EntityManagerFactoryCreator {

    public EclipseLinkEntityManagerFactoryCreator(Connection connection) {
        super(connection);
    }

    @Override
    public EntityManagerFactory createEntityManagerFactory() {
        return Persistence.createEntityManagerFactory("EclipseLink", getMapProperties());
    }
}
