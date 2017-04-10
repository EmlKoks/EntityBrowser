package emlkoks.entitybrowser.connection;

import org.hibernate.jpa.AvailableSettings;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
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

    public static EntityManagerFactory createConnection(Connection connection, List<Class> classList){
        Map<String, Object> properties = new HashMap<String, Object>();

        connection.getDriver().loadDriver();

        properties.put("javax.persistence.jdbc.driver", connection.getDriver().getClassName());
        properties.put("javax.persistence.jdbc.url", connection.getUrl());
        properties.put("javax.persistence.jdbc.user", connection.getUser());
        properties.put("javax.persistence.jdbc.password", connection.getPassword());
        properties.put(AvailableSettings.LOADED_CLASSES, classList);

        return Persistence.createEntityManagerFactory("persistance", properties);
    }

    public static boolean testConnection(Connection c){
        c.getDriver().loadDriver();
        try {
            DriverManager.getConnection(c.getUrl(), c.getUser(), c.getPassword());
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
