package emlkoks.entitybrowser.mocked;

import emlkoks.entitybrowser.mocked.entity.MockedEntity1;
import emlkoks.entitybrowser.session.Entity;
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
        super(null, null, null);
        prepareEntityList();
    }

    private void prepareEntityList() {
        Map<String, Entity> classMap = new HashMap<>();
        classMap.put("Test1", new Entity(MockedEntity1.class));
        this.entityList.setClassMap(classMap);
    }

    @Override
    public CriteriaBuilder getCriteriaBuilder() {
        return null;
    }

    public static List getResultsList() {
        List results = new ArrayList();
        results.add(new MockedEntity1(1, new Date()));
//        results.add(new MockedEntity1(1, "str1", 1, true, true, new Date(), VALUE_1));
//        results.add(new MockedEntity1(2, "str2", 2, true, true, new Date(), VALUE_2));
//        results.add(new MockedEntity1(3, "str3", 3, true, true, new Date(), VALUE_3));
//        results.add(new MockedEntity1(4, "str4", 4, true, true, new Date(), VALUE_3));
        return results;
    }
}
