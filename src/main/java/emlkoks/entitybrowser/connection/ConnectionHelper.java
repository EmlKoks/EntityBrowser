package emlkoks.entitybrowser.connection;

import emlkoks.entitybrowser.connection.factory.creator.EclipseLinkEntityManagerFactoryCreator;
import emlkoks.entitybrowser.connection.factory.creator.EntityManagerFactoryCreator;
import emlkoks.entitybrowser.connection.factory.creator.HibernateEntityManagerFactory;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import javax.persistence.EntityManagerFactory;

/**
 * Created by EmlKoks on 10.03.17.
 */
public class ConnectionHelper {

    public static EntityManagerFactory createConnection(
            Connection connection, List<Class> classList, Provider provider) {
        connection.getDriver().loadDriver();


        EntityManagerFactoryCreator entityManagerFactoryCreator;
        switch (provider) {
            case Hibernate:
                entityManagerFactoryCreator = new HibernateEntityManagerFactory(connection, classList);
                break;
            case EclipseLink:
                entityManagerFactoryCreator = new EclipseLinkEntityManagerFactoryCreator(connection);
                break;
            default:
                throw new EntityManagerNotFoundException(provider);
        }

        return entityManagerFactoryCreator.createEntityManagerFactory();
    }

    public static boolean testConnection(Connection c) {
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
