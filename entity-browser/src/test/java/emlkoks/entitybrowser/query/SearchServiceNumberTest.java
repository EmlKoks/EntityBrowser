package emlkoks.entitybrowser.query;

import emlkoks.entitybrowser.connection.provider.TestProvider;
import emlkoks.entitybrowser.query.comparator.ComparationType;
import emlkoks.entitybrowser.session.entity.ClassDetails;
import emlkoks.entitybrowser.session.entity.FieldProperty;
import java.util.Arrays;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import test.EntityWithInteger;

import static org.junit.Assert.assertEquals;

public class SearchServiceNumberTest {
    private TestProvider provider;
    private ClassDetails classDetails = new ClassDetails(EntityWithInteger.class);
    private SearchService searchService;
    private FieldProperty fieldProperty;

    @Before
    public void initalize() {
        provider = new TestProvider(EntityWithInteger.class);
        searchService = new SearchService(provider.getProvider());
        fieldProperty = classDetails.getFieldProperty("testInteger");
        prepareTestData();
    }

    @After
    public void cleanupTransaction() {
        var transaction = provider.getProvider().getEntityManager().getTransaction();
        if (transaction.isActive()) {
            transaction.rollback();
        }
    }

    private void prepareTestData() {
        provider.addEntities(
                new EntityWithInteger(null),
                new EntityWithInteger(null),
                new EntityWithInteger(1),
                new EntityWithInteger(1),
                new EntityWithInteger(2),
                new EntityWithInteger(3),
                new EntityWithInteger(4),
                new EntityWithInteger(4),
                new EntityWithInteger(5),
                new EntityWithInteger(6),
                new EntityWithInteger(6),
                new EntityWithInteger(6));
    }

    @Test
    public void test() {
        assertEquals(2, doSearch(ComparationType.IS_NULL));
        assertEquals(10, doSearch(ComparationType.IS_NOT_NULL));
        assertEquals(2, doSearch(ComparationType.EQUAL, 4));
        assertEquals(8, doSearch(ComparationType.NOT_EQUAL, 4));
        assertEquals(6, doSearch(ComparationType.GREATER, 3));
        assertEquals(7, doSearch(ComparationType.GREATER_OR_EQUAL, 3));
        assertEquals(3, doSearch(ComparationType.LESS, 3));
        assertEquals(4, doSearch(ComparationType.LESS_OR_EQUAL, 3));
        assertEquals(5, doSearch(ComparationType.BETWEEN, 2, 5));
    }

    private int doSearch(ComparationType comparationType, Object... value) {
        var fieldFilter = new FieldFilter(comparationType, fieldProperty, value);
        return searchService.search(classDetails, Arrays.asList(fieldFilter)).getResults().size();
    }
}
