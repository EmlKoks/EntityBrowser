package emlkoks.entitybrowser.connection;

import emlkoks.entitybrowser.connection.factoryCreator.EclipseLinkEntityManagerFactoryCreator;
import emlkoks.entitybrowser.connection.factoryCreator.EntityManagerFactoryCreator;
import emlkoks.entitybrowser.connection.factoryCreator.HibernateEntityManagerFactory;

import javax.persistence.EntityManagerFactory;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by EmlKoks on 10.03.17.
 */
public class Connector {

    public static EntityManagerFactory createConnection(Connection connection, List<Class> classList, ProviderEnum provider){
        connection.getDriver().loadDriver();


        EntityManagerFactoryCreator entityManagerFactoryCreator;
        if(provider == ProviderEnum.Hibernate) {
            entityManagerFactoryCreator = new HibernateEntityManagerFactory(connection, classList);
        }
         else if(provider == ProviderEnum.EclipseLink) {
            entityManagerFactoryCreator = new EclipseLinkEntityManagerFactoryCreator(connection);
        } else {
            throw new RuntimeException("Provider not defined");
        }

        return entityManagerFactoryCreator.createEntityManagerFactory();
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
