package emlkoks.entitybrowser;

import emlkoks.entitybrowser.drivers.Driver;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by koks on 10.03.17.
 */
public class Connector {
    public static EntityManagerFactory createConnection(Driver driver, String user, String pass, String url){
        Map<String, Object> properties = new HashMap<String, Object>();

        driver.loadDriver();

        properties.put("javax.persistence.jdbc.driver", driver.getClassName());
        properties.put("javax.persistence.jdbc.url", url);
        properties.put("javax.persistence.jdbc.user", user);
        properties.put("javax.persistence.jdbc.password", pass);

        return Persistence.createEntityManagerFactory("persistance", properties);
    }
}
