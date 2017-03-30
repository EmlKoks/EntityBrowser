package emlkoks.entitybrowser.connection;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by EmlKoks on 10.03.17.
 */
public class Connector {
    public static EntityManagerFactory createConnection(Connection connection){
        Map<String, Object> properties = new HashMap<String, Object>();

        connection.getDriver().loadDriver();

        properties.put("javax.persistence.jdbc.driver", connection.getDriver().getClassName());
        properties.put("javax.persistence.jdbc.url", connection.getUrl());
        properties.put("javax.persistence.jdbc.user", connection.getUser());
        properties.put("javax.persistence.jdbc.password", connection.getPassword());

        return Persistence.createEntityManagerFactory("persistance", properties);
    }
}
