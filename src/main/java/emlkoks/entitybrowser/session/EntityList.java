package emlkoks.entitybrowser.session;

import emlkoks.entitybrowser.common.LibraryManager;
import lombok.Data;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by EmlKoks on 03.04.17.
 */
@Data
public class EntityList {
    private Map<String, Entity> classMap;

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

    public Entity getEntity(String entityName) {
        return classMap.get(entityName);
    }

}
