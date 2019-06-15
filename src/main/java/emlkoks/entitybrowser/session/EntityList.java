package emlkoks.entitybrowser.session;

import emlkoks.entitybrowser.common.LibraryManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by EmlKoks on 03.04.17.
 */
class EntityList {
    Map<String, Entity> classMap;

    EntityList(File libFile) {
        classMap = LibraryManager.getEntitesFromLib(libFile);
    }

    List<Class> getClasses() {
        List<Class> list = new ArrayList<>();
        classMap.values().forEach(x -> list.add(x.getClazz()));
        return list;
    }

    List<String> getClassNames() {
        ArrayList<String> names = new ArrayList<>();
        classMap.keySet().forEach(x -> names.add(x));
        return names;
    }

    Entity getEntity(String entityName) {
        return classMap.get(entityName);
    }

}
