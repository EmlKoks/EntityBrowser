package emlkoks.entitybrowser.mocked;

import emlkoks.entitybrowser.connection.Connection;
import emlkoks.entitybrowser.session.entity.ClassDetails;
import emlkoks.entitybrowser.session.entity.EntityList;
import emlkoks.entitybrowser.mocked.entity.MockedEntity1;
import emlkoks.entitybrowser.mocked.entity.MockedEntity2;
import emlkoks.entitybrowser.session.SearchResults;
import emlkoks.entitybrowser.session.Session;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.criteria.CriteriaBuilder;

import static emlkoks.entitybrowser.mocked.entity.ExampleEnum.*;

public class MockSession extends Session {
    public MockSession() {
        super(new Connection());
        prepareEntityList();
    }

    private void prepareEntityList() {
        Map<String, ClassDetails> classMap = new HashMap<>();
        classMap.put("Test1", new ClassDetails(MockedEntity1.class));
        classMap.put("Test2", new ClassDetails(MockedEntity2.class));
        this.entityList = new EntityList(null);
        this.entityList.setClassMap(classMap);
    }

    @Override
    public CriteriaBuilder getCriteriaBuilder() {
        return null;
    }

    public static SearchResults getSearchResults() {
        List<Object> results = new ArrayList<>();
//        results.add(new MockedEntity1(1, new Date()));
        results.add(new MockedEntity1(1, "str1", 1, 0.0, true, true, new Date(), VALUE_1));
        results.add(new MockedEntity1(2, "str2", 2, 0.0,true, true, new Date(), VALUE_2));
        results.add(new MockedEntity1(3, "str3", 3, 0.0,true, true, new Date(), VALUE_3));
        results.add(new MockedEntity1(4, "str4", 4, 0.0,true, true, new Date(), VALUE_3));
        return new SearchResults(new ClassDetails(MockedEntity1.class), results);
    }
}
