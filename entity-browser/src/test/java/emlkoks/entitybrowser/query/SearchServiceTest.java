package emlkoks.entitybrowser.query;

import emlkoks.entitybrowser.connection.provider.TestProvider;
import emlkoks.entitybrowser.session.entity.ClassDetails;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import test.EntityWithInteger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SearchServiceTest {
    private TestProvider provider;
    private ClassDetails classDetails = new ClassDetails(EntityWithInteger.class);
    private SearchService searchService;

    @Before
    public void initalize() {
        provider = new TestProvider(EntityWithInteger.class);
        searchService = new SearchService(provider.getProvider());
    }

    @After
    public void cleanupTransaction() {
        var transaction = provider.getProvider().getEntityManager().getTransaction();
        if (transaction.isActive()) {
            transaction.rollback();
        }
    }

    @Test
    public void searchWithoutResults() {
        assertTrue(searchService.search(classDetails, null).getResults().isEmpty());
    }

    @Test
    public void searchResults() {
        provider.addEntities(2);
        assertEquals(2, searchService.search(classDetails, null).getResults().size());
    }
}