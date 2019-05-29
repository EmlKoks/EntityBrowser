package emlkoks.entitybrowser.session;

import emlkoks.entitybrowser.common.LibraryManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by EmlKoks on 03.04.17.
 */
public class EntityList {
    Map<String, Entity> classMap;

    public EntityList(File libFile) {
        classMap = LibraryManager.getEntitesFromLib(libFile);
    }

    public List<Class> getClasses(){
        List<Class> list = new ArrayList<>();
        classMap.values().forEach(x -> list.add(x.getClazz()));
        return list;
    }

    public List<String> getClassNames(){
        ArrayList<String> names = new ArrayList<>();
        classMap.keySet().forEach(x -> names.add(x));
        return names;
    }

    public Entity getEntity(String entityName){
        return classMap.get(entityName);
    }

}
