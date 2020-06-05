package emlkoks.entitybrowser.connection.provider;

import emlkoks.entitybrowser.connection.Connection;
import emlkoks.entitybrowser.session.entity.EntityList;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class EclipseLinkProvider extends JpaProvider {

    public EclipseLinkProvider(Connection connection) {
        super(connection);
    }

    @Override
    public EntityManagerFactory createEntityManagerFactory(EntityList entityList) {
        return Persistence.createEntityManagerFactory("EclipseLink", getMapProperties());
    }
}
