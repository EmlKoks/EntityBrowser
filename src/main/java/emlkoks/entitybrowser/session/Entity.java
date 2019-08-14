package emlkoks.entitybrowser.session;

import emlkoks.entitybrowser.common.LibraryManager;
import java.util.Collection;
import java.util.Set;
import java.util.SortedMap;
import lombok.Data;

/**
 * Created by EmlKoks on 09.04.17.
 */
@Data
public class Entity {
    private String name;
    private Class clazz;
    private Class metamodel;
    private SortedMap<String, FieldProperty> fields;

    public Entity(Class entity) {
        name = entity.getName();
        clazz = entity;
    }

    public Set<String> getFieldsNames() {
        if (fields == null) {
            fields = LibraryManager.getEntityFields(this);
        }
        return fields.keySet();
    }

    public FieldProperty getField(String name) {
        if (fields == null) {
            fields = LibraryManager.getEntityFields(this);
        }
        return fields.get(name);
    }

    public Collection<FieldProperty> getFields() {
        if (fields == null) {
            fields = LibraryManager.getEntityFields(this);
        }
        return fields.values();
    }

    public FieldProperty getFieldProperty(String name) {
        if (fields == null) {
            fields = LibraryManager.getEntityFields(this);
        }
        return fields.get(name);
    }

}
