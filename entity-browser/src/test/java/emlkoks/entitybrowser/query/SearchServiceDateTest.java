package emlkoks.entitybrowser.query;

import emlkoks.entitybrowser.connection.provider.TestProvider;
import emlkoks.entitybrowser.query.comparator.ComparationType;
import emlkoks.entitybrowser.session.entity.ClassDetails;
import emlkoks.entitybrowser.session.entity.FieldProperty;
import java.time.LocalDate;
import java.util.Arrays;
import org.junit.Before;
import org.junit.Test;
import test.EntityWithDate;

import static org.junit.Assert.assertEquals;

public class SearchServiceDateTest {
    private TestProvider provider;
    private ClassDetails classDetails = new ClassDetails(EntityWithDate.class);
    private SearchService searchService;
    private FieldProperty fieldProperty;

    @Before
    public void initalize() {
        provider = new TestProvider(EntityWithDate.class);
        searchService = new SearchService(provider.getProvider());
        fieldProperty = classDetails.getFieldProperty("testDate");
        prepareTestData();
    }

    private void prepareTestData() {
        provider.addEntities(
                new EntityWithDate(null),
                new EntityWithDate(null),
                new EntityWithDate(LocalDate.of(2020, 1, 1)),
                new EntityWithDate(LocalDate.of(2020, 1, 1)),
                new EntityWithDate(LocalDate.of(2020, 2, 1)),
                new EntityWithDate(LocalDate.of(2020, 4, 1)),
                new EntityWithDate(LocalDate.of(2020, 5, 1)),
                new EntityWithDate(LocalDate.of(2020, 6, 1)),
                new EntityWithDate(LocalDate.of(2020, 7, 1)),
                new EntityWithDate(LocalDate.of(2020, 8, 1)),
                new EntityWithDate(LocalDate.of(2020, 9, 1)),
                new EntityWithDate(LocalDate.of(2020, 10, 1)));
    }

    @Test
    public void test() {
        assertEquals(2, doSearch(ComparationType.IS_NULL));
        assertEquals(10, doSearch(ComparationType.IS_NOT_NULL));
        assertEquals(2, doSearch(ComparationType.EQUAL, LocalDate.of(2020, 1, 1)));
        assertEquals(8, doSearch(ComparationType.NOT_EQUAL, LocalDate.of(2020, 1, 1)));
        assertEquals(5, doSearch(ComparationType.GREATER, LocalDate.of(2020, 5, 1)));
        assertEquals(6, doSearch(ComparationType.GREATER_OR_EQUAL, LocalDate.of(2020, 5, 1)));
        assertEquals(2, doSearch(ComparationType.LESS, LocalDate.of(2020, 2, 1)));
        assertEquals(3, doSearch(ComparationType.LESS_OR_EQUAL, LocalDate.of(2020, 2, 1)));
        assertEquals(4, doSearch(ComparationType.BETWEEN, LocalDate.of(2020, 2, 1),
                LocalDate.of(2020, 6, 1)));
    }

    private int doSearch(ComparationType comparationType, Object... value) {
        var fieldFilter = new FieldFilter(comparationType, fieldProperty, value);
        return searchService.search(classDetails, Arrays.asList(fieldFilter)).getResults().size();
    }
}
