package emlkoks.entitybrowser.connection.factory.creator;

import emlkoks.entitybrowser.connection.Connection;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.EntityManagerFactory;

/**
 * Created by EmlKoks on 10.03.17.
 */
public abstract class EntityManagerFactoryCreator {
    protected Map<String, Object> properties = new HashMap<String, Object>();

    EntityManagerFactoryCreator(Connection connection) {
        properties.put("javax.persistence.jdbc.driver", connection.getDriver().getClassName());
        properties.put("javax.persistence.jdbc.url", connection.getUrl());
        properties.put("javax.persistence.jdbc.user", connection.getUser());
        properties.put("javax.persistence.jdbc.password", connection.getPassword());
    }

    public abstract EntityManagerFactory createEntityManagerFactory();
}
