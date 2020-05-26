package emlkoks.entitybrowser.entity;

import emlkoks.entitybrowser.common.LibraryManager;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lombok.Data;

/**
 * Created by EmlKoks on 03.04.17.
 */
@Data
public class EntityList {
    private Map<String, EntityDetails> classMap;

    public EntityList(File libFile) {
        if (libFile != null) {
            classMap = LibraryManager.getEntitesFromLib(libFile);
        }
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

    public EntityDetails getEntity(String entityName) {
        return classMap.get(entityName);
    }

}
