package emlkoks.entitybrowser.query;

import emlkoks.entitybrowser.connection.provider.TestProvider;
import emlkoks.entitybrowser.query.comparator.ComparationType;
import emlkoks.entitybrowser.session.entity.ClassDetails;
import emlkoks.entitybrowser.session.entity.FieldProperty;
import java.util.Arrays;
import org.junit.Before;
import org.junit.Test;
import test.EntityWithCharacter;

import static org.junit.Assert.assertEquals;

public class SearchServiceCharacterTest {
    private TestProvider provider;
    private ClassDetails classDetails = new ClassDetails(EntityWithCharacter.class);
    private SearchService searchService;
    private FieldProperty fieldProperty;

    @Before
    public void initalize() {
        provider = new TestProvider(EntityWithCharacter.class);
        searchService = new SearchService(provider.getProvider());
        fieldProperty = classDetails.getFieldProperty("testCharacter");
        prepareTestData();
    }

    private void prepareTestData() {
        provider.addEntities(
                new EntityWithCharacter(null),
                new EntityWithCharacter(null),
                new EntityWithCharacter('a'),
                new EntityWithCharacter('b'),
                new EntityWithCharacter('c'),
                new EntityWithCharacter('a'),
                new EntityWithCharacter(null),
                new EntityWithCharacter('a'),
                new EntityWithCharacter('b'));
    }

    @Test
    public void test() {
        assertEquals(3, doSearch(ComparationType.IS_NULL, null));
        assertEquals(6, doSearch(ComparationType.IS_NOT_NULL, null));
        assertEquals(3, doSearch(ComparationType.EQUAL, 'a'));
        assertEquals(2, doSearch(ComparationType.EQUAL, 'b'));
        assertEquals(3, doSearch(ComparationType.NOT_EQUAL, 'a'));
    }

    private int doSearch(ComparationType comparationType, Object value) {
        var fieldFilter = new FieldFilter(comparationType, fieldProperty, value);
        return searchService.search(classDetails, Arrays.asList(fieldFilter)).getResults().size();
    }
}
