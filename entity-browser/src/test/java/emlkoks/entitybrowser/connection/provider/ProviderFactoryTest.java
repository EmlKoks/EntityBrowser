package emlkoks.entitybrowser.connection.provider;

import emlkoks.entitybrowser.connection.Connection;
import emlkoks.entitybrowser.connection.Driver;
import emlkoks.entitybrowser.connection.Provider;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.instanceOf;

public class ProviderFactoryTest {

    @Test
    public void getHibernateProvider() {
        var driver = new Driver();
        driver.setClassName("className");
        var connection = new Connection();
        connection.setDriver(driver);
        connection.setUrl("url");
        connection.setProvider(Provider.Hibernate);
        var jpaProvider = ProviderFactory.getProvider(connection);
        assertThat(jpaProvider, instanceOf(HibernateProvider.class));
    }

    @Test
    public void getEclipseLinkProvider() {
        var driver = new Driver();
        driver.setClassName("className");
        var connection = new Connection();
        connection.setDriver(driver);
        connection.setUrl("url");
        connection.setProvider(Provider.EclipseLink);
        var jpaProvider = ProviderFactory.getProvider(connection);
        assertThat(jpaProvider, instanceOf(EclipseLinkProvider.class));
    }

    @Test(expected = RuntimeException.class)
    public void getWrongProvider() {
        ProviderFactory.getProvider(new Connection());
    }

}