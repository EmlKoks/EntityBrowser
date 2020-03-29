package mock.mock;

import emlkoks.entitybrowser.session.Entity;
import emlkoks.entitybrowser.session.Session;

import java.util.HashMap;
import java.util.Map;

public class MockSession extends Session {
    public MockSession() {
        super(null, null, null);
        prepareEntityList();
    }

    private void prepareEntityList() {
        Map<String, Entity> classMap = new HashMap<>();
        this.entityList.setClassMap(classMap);
    }
}
