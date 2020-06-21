package emlkoks.entitybrowser.query;

import emlkoks.entitybrowser.connection.provider.TestProvider;
import emlkoks.entitybrowser.query.comparator.ComparationType;
import emlkoks.entitybrowser.session.entity.ClassDetails;
import emlkoks.entitybrowser.session.entity.FieldProperty;
import java.util.Arrays;
import org.junit.Before;
import org.junit.Test;
import test.EntityWithString;

import static org.junit.Assert.assertEquals;

public class SearchServiceStringTest {
    private TestProvider provider;
    private ClassDetails classDetails = new ClassDetails(EntityWithString.class);
    private SearchService searchService;
    private FieldProperty fieldProperty;

    @Before
    public void initalize() {
        provider = new TestProvider(EntityWithString.class);
        searchService = new SearchService(provider.getProvider());
        fieldProperty = classDetails.getFieldProperty("testString");
        prepareTestData();
    }

    private void prepareTestData() {
        provider.addEntities(
                new EntityWithString(null),
                new EntityWithString(null),
                new EntityWithString("ab"),
                new EntityWithString("abc"),
                new EntityWithString("abc"),
                new EntityWithString("cde"),
                new EntityWithString("acdd"),
                new EntityWithString("bcdd"),
                new EntityWithString("dd"));
    }

    @Test
    public void test() {
        assertEquals(2, doSearch(ComparationType.IS_NULL));
        assertEquals(7, doSearch(ComparationType.IS_NOT_NULL));
        assertEquals(2, doSearch(ComparationType.EQUAL, "abc"));
        assertEquals(5, doSearch(ComparationType.NOT_EQUAL, "abc"));
        assertEquals(3, doSearch(ComparationType.CONTAINS, "bc"));
    }

    private int doSearch(ComparationType comparationType, Object... value) {
        var fieldFilter = new FieldFilter(comparationType, fieldProperty, value);
        return searchService.search(classDetails, Arrays.asList(fieldFilter)).getResults().size();
    }
}
