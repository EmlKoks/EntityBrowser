package emlkoks.entitybrowser.query;

import emlkoks.entitybrowser.connection.provider.HibernateProvider;
import emlkoks.entitybrowser.connection.provider.HibernateProviderTest;
import emlkoks.entitybrowser.connection.provider.JpaProvider;
import emlkoks.entitybrowser.session.entity.ClassDetails;
import emlkoks.entitybrowser.session.entity.EntityList;
import org.junit.Before;
import org.junit.Test;
import test.TestEntity;

import java.util.Arrays;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SearchServiceTest {
    private JpaProvider provider;
    private EntityList entityList = mock(EntityList.class);

    @Before
    public void initalizeProvider() {
        provider = new HibernateProvider(HibernateProviderTest.createH2Connection());
    }

    @Test
    public void search() {
        when(entityList.getClasses()).thenReturn(Arrays.asList(TestEntity.class));
        when(entityList.hasClasses()).thenReturn(true);
        provider.connect(entityList);
        var searchService = new SearchService(provider);
        var classDetails = new ClassDetails(TestEntity.class);
//        provider.getEntityManager().createQuery("SELECT id FROM TestEntity").getResultList();
//        provider.getEntityManager().persist();
        searchService.search(classDetails, null);
    }

}