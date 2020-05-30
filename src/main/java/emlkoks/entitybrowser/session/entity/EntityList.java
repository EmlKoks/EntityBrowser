package emlkoks.entitybrowser.session.entity;

import emlkoks.entitybrowser.common.LibraryManager;
import emlkoks.entitybrowser.session.exception.LibraryFileCannotBeNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by EmlKoks on 03.04.17.
 */
public class EntityList {
    private Map<String, ClassDetails> classMap;

    public EntityList(File libFile) {
        if (Objects.isNull(libFile)) {
            throw new LibraryFileCannotBeNull();
        }
        classMap = LibraryManager.getEntitesFromLib(libFile);
    }

    public List<Class> getClasses() {
        List<Class> list = new ArrayList<>();
        classMap.values().forEach(x -> list.add(x.getClazz()));
        return list;
    }

    public List<String> getClassNames() {
        ArrayList<String> names = new ArrayList<>();
        classMap.keySet().forEach(x -> names.add(x));
        return names;
    }

    public ClassDetails getEntity(String entityName) {
        return classMap.get(entityName);
    }

}
