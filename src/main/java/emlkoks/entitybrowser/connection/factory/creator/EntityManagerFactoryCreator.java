package emlkoks.entitybrowser.connection.factory.creator;

import emlkoks.entitybrowser.connection.Connection;
import emlkoks.entitybrowser.connection.Property;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.EntityManagerFactory;

/**
 * Created by EmlKoks on 10.03.17.
 */
public abstract class EntityManagerFactoryCreator {
    protected Set<Property> properties = new HashSet<>();

    EntityManagerFactoryCreator(Connection connection) {
        properties.add(new Property("javax.persistence.jdbc.driver", connection.getDriver().getClassName()));
        properties.add(new Property("javax.persistence.jdbc.url", connection.getUrl()));
        properties.add(new Property("javax.persistence.jdbc.user", connection.getUser()));
        properties.add(new Property("javax.persistence.jdbc.password", connection.getPassword()));
    }

    protected Map<String, Object> getMapProperties() {
        return properties.stream()
            .collect(
                Collectors.toMap(
                    Property::getName,
                    Property::getValue
                ));
    }

    public abstract EntityManagerFactory createEntityManagerFactory();

    public Set<Property> getProperties() {
        return properties;
    }
}
