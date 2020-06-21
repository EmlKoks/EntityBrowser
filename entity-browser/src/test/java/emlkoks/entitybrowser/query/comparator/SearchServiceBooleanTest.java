package emlkoks.entitybrowser.query.comparator;

import emlkoks.entitybrowser.connection.provider.TestProvider;
import emlkoks.entitybrowser.query.FieldFilter;
import emlkoks.entitybrowser.query.SearchService;
import emlkoks.entitybrowser.session.entity.ClassDetails;
import emlkoks.entitybrowser.session.entity.FieldProperty;
import org.junit.Before;
import org.junit.Test;
import test.EntityWithBoolean;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class SearchServiceBooleanTest {
    private TestProvider provider;
    private ClassDetails classDetails = new ClassDetails(EntityWithBoolean.class);
    private SearchService searchService;
    private FieldProperty fieldProperty;

    @Before
    public void initalize() {
        provider = new TestProvider(EntityWithBoolean.class);
        searchService = new SearchService(provider.getProvider());
        fieldProperty = classDetails.getFieldProperty("testBoolean");
        prepareTestData();
    }

    private void prepareTestData() {
        provider.addEntities(
                new EntityWithBoolean(null),
                new EntityWithBoolean(null),
                new EntityWithBoolean(false),
                new EntityWithBoolean(true),
                new EntityWithBoolean(true),
                new EntityWithBoolean(false),
                new EntityWithBoolean(false));
    }

    @Test
    public void test() {
        assertEquals(2, doSearch(ComparationType.IS_NULL, null));
        assertEquals(5, doSearch(ComparationType.IS_NOT_NULL, null));
        assertEquals(2, doSearch(ComparationType.EQUAL, true));
        assertEquals(3, doSearch(ComparationType.NOT_EQUAL, true));
    }

    private int doSearch(ComparationType comparationType, Object value) {
        var fieldFilter = new FieldFilter(comparationType, fieldProperty, value);
        return searchService.search(classDetails, Arrays.asList(fieldFilter)).getResults().size();
    }
}
