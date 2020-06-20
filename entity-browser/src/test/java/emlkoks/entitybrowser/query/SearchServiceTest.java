package emlkoks.entitybrowser.query;

import emlkoks.entitybrowser.connection.ConnectionTest;
import emlkoks.entitybrowser.connection.provider.HibernateProvider;
import emlkoks.entitybrowser.connection.provider.JpaProvider;
import emlkoks.entitybrowser.query.comparator.ComparationType;
import emlkoks.entitybrowser.session.entity.ClassDetails;
import emlkoks.entitybrowser.session.entity.EntityList;
import java.util.Arrays;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import test.TestEntity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SearchServiceTest {
    private JpaProvider provider;
    private EntityList entityList = mock(EntityList.class);

    @Before
    public void initalize() {
        provider = new HibernateProvider(ConnectionTest.createH2Connection());
        when(entityList.getClasses()).thenReturn(Arrays.asList(TestEntity.class));
        when(entityList.hasClasses()).thenReturn(true);
        provider.connect(entityList);
    }

    @After
    public void cleanupTransaction() {
        var transaction = provider.getEntityManager().getTransaction();
        if (transaction.isActive()) {
            transaction.rollback();
            transaction.begin();
        }
    }

    @Test
    public void searchWithoutResults() {
        var searchService = new SearchService(provider);
        var classDetails = new ClassDetails(TestEntity.class);
        assertTrue(searchService.search(classDetails, null).getResults().isEmpty());
    }

    @Test
    public void searchResults() {
        addEntities(2);
        var searchService = new SearchService(provider);
        var classDetails = new ClassDetails(TestEntity.class);
        assertEquals(2, searchService.search(classDetails, null).getResults().size());
    }

    @Test
    public void testBooleanValues() {
        addEntities(
                new TestEntity(null),
                new TestEntity(null),
                new TestEntity(true),
                new TestEntity(false),
                new TestEntity(false));
        var searchService = new SearchService(provider);
        var classDetails = new ClassDetails(TestEntity.class);
        var fieldProperty = classDetails.getFieldProperty("testBoolean");
        assertEquals(2, searchService.search(classDetails, Arrays.asList(new FieldFilter(ComparationType.IS_NULL, fieldProperty, null))).getResults().size());
        assertEquals(3, searchService.search(classDetails, Arrays.asList(new FieldFilter(ComparationType.IS_NOT_NULL, fieldProperty, null))).getResults().size());
        assertEquals(1, searchService.search(classDetails, Arrays.asList(new FieldFilter(ComparationType.EQUAL, fieldProperty, true))).getResults().size());
//        assertEquals(4, searchService.search(classDetails, Arrays.asList(new FieldFilter(ComparationType.NOT_EQUAL, fieldProperty, true))).getResults().size());
    }

    private void addEntities(int numberOfEntities) {
        var em = provider.getEntityManager();
        em.getTransaction().begin();
        LongStream.range(0, numberOfEntities)
                .mapToObj(i -> {
                    var entity = new TestEntity();
                    entity.setId(i);
                    return entity;
                })
                .forEach(em::persist);
    }

    private void addEntities(TestEntity... entities) {
        var em = provider.getEntityManager();
        em.getTransaction().begin();
        IntStream.range(0, entities.length)
                .mapToObj(i -> {
                    entities[i].setId((long)i);
                    return entities[i];
                })
                .forEach(em::persist);
    }
}