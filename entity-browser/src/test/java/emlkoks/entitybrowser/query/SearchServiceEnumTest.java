package emlkoks.entitybrowser.query;

import emlkoks.entitybrowser.connection.provider.TestProvider;
import emlkoks.entitybrowser.query.comparator.ComparationType;
import emlkoks.entitybrowser.session.entity.ClassDetails;
import emlkoks.entitybrowser.session.entity.FieldProperty;
import java.util.Arrays;
import org.junit.Before;
import org.junit.Test;
import test.EntityWithEnum;
import test.TestEnum;

import static org.junit.Assert.assertEquals;

public class SearchServiceEnumTest {
    private TestProvider provider;
    private ClassDetails classDetails = new ClassDetails(EntityWithEnum.class);
    private SearchService searchService;
    private FieldProperty fieldProperty;

    @Before
    public void initalize() {
        provider = new TestProvider(EntityWithEnum.class);
        searchService = new SearchService(provider.getProvider());
        fieldProperty = classDetails.getFieldProperty("testEnum");
        prepareTestData();
    }

    private void prepareTestData() {
        provider.addEntities(
                new EntityWithEnum(null),
                new EntityWithEnum(null),
                new EntityWithEnum(TestEnum.A),
                new EntityWithEnum(TestEnum.A),
                new EntityWithEnum(TestEnum.B),
                new EntityWithEnum(TestEnum.B),
                new EntityWithEnum(TestEnum.B),
                new EntityWithEnum(TestEnum.C));
    }

    @Test
    public void test() {
        assertEquals(2, doSearch(ComparationType.IS_NULL, null));
        assertEquals(6, doSearch(ComparationType.IS_NOT_NULL, null));
        assertEquals(2, doSearch(ComparationType.EQUAL, TestEnum.A));
        assertEquals(4, doSearch(ComparationType.NOT_EQUAL, TestEnum.A));
    }

    private int doSearch(ComparationType comparationType, Object value) {
        var fieldFilter = new FieldFilter(comparationType, fieldProperty, value);
        return searchService.search(classDetails, Arrays.asList(fieldFilter)).getResults().size();
    }
}
