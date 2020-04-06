package emlkoks.entitybrowser.mocked;

import emlkoks.entitybrowser.mocked.entity.MockedEntity1;
import emlkoks.entitybrowser.session.Entity;
import emlkoks.entitybrowser.session.Session;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.criteria.CriteriaBuilder;

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
        results.add(new MockedEntity1(1, "str1", 1));
        results.add(new MockedEntity1(2, "str2", 2));
        results.add(new MockedEntity1(3, "str3", 3));
        results.add(new MockedEntity1(4, "str4", 4));
        return results;
    }
}
