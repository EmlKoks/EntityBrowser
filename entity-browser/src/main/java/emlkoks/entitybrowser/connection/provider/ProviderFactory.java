package emlkoks.entitybrowser.connection.provider;

import emlkoks.entitybrowser.connection.Connection;

public class ProviderFactory {

    public JpaProvider getProvider(Connection connection) {
        switch (connection.getProvider()) {
            case Hibernate:
                return new HibernateProvider(connection);
            case EclipseLink:
                return new EclipseLinkProvider(connection);
            default:
                throw new RuntimeException();//TODO
        }
    }
}
