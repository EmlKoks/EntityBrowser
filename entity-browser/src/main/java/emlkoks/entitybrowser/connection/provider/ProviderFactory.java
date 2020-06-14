package emlkoks.entitybrowser.connection.provider;

import emlkoks.entitybrowser.connection.Connection;
import java.util.Objects;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ProviderFactory {

    public JpaProvider getProvider(Connection connection) {
        if (Objects.isNull(connection.getProvider())) {
            throw new IllegalArgumentException("Connection provider cannot be null");
        }
        switch (connection.getProvider()) {
            case Hibernate:
                return new HibernateProvider(connection);
            case EclipseLink:
                return new EclipseLinkProvider(connection);
        }
        throw new RuntimeException();
    }
}
