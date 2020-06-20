package emlkoks.entitybrowser.query;

import emlkoks.entitybrowser.session.entity.ClassDetails;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.Test;
import test.TestEntity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SearchResultsTest {

    @Test
    public void createEmptySearchResults() {
        ClassDetails classDetails = new ClassDetails(TestEntity.class);

        SearchResults searchResults = new SearchResults(classDetails, Collections.emptyList());
        assertTrue(searchResults.getResults().isEmpty());
        assertEquals(classDetails, searchResults.getClassDetails());
    }

    @Test
    public void createEmptySearchResultsByNullresults() {
        ClassDetails classDetails = new ClassDetails(TestEntity.class);

        SearchResults searchResults = new SearchResults(classDetails, null);
        assertTrue(searchResults.getResults().isEmpty());
        assertEquals(classDetails, searchResults.getClassDetails());
    }

    @Test
    public void createNotEmptySearchResults() {
        ClassDetails classDetails = new ClassDetails(TestEntity.class);
        List<Object> resultsList = Arrays.asList(new TestEntity(), new TestEntity(), new TestEntity());
        SearchResults searchResults = new SearchResults(classDetails, resultsList);
        assertEquals(3, searchResults.getResults().size());
        assertEquals(classDetails, searchResults.getClassDetails());
    }

}