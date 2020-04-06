package emlkoks.entitybrowser.session;

import emlkoks.entitybrowser.common.LibraryManager;

import java.util.Collection;
import java.util.Set;
import java.util.SortedMap;
import lombok.Getter;

/**
 * Created by EmlKoks on 09.04.17.
 */
public class Entity {

    private String name;
    @Getter
    private Class clazz;
    private Class metamodel;
    private SortedMap<String, FieldProperty> fields;

    public Entity(Class entity) {
        name = entity.getName();
        clazz = entity;
    }

    public String getSimpleName() {
        return clazz.getSimpleName();
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

    public Object getIdValue(Object entityObject) {
        if (!entityObject.getClass().equals(clazz)) {
            throw new RuntimeException("Wrong object type. Is " + entityObject.getClass().getName()
                    + " but should be " + clazz.getName());
        }
        return fields.values().stream()
                .filter(FieldProperty::isId)
                .findFirst()
                .map(fieldProperty -> fieldProperty.getValue(entityObject))
                .orElse(null);
    }

}
