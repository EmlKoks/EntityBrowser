package emlkoks.entitybrowser.session.entity;

import emlkoks.entitybrowser.session.exception.LibraryFileNotFoundException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by EmlKoks on 03.04.17.
 */
public class EntityList {
    private Map<String, ClassDetails> classMap;

    public EntityList(File libFile) throws LibraryFileNotFoundException {
        classMap = new EntityLibraryLoader(libFile).load().stream()
                .collect(Collectors.toMap(ClassDetails::getFullName, classDetails -> classDetails));
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
