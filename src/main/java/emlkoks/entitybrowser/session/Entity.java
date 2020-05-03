package emlkoks.entitybrowser.session;

import emlkoks.entitybrowser.common.LibraryManager;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.Getter;

/**
 * Created by EmlKoks on 09.04.17.
 */
public class Entity {
    @Getter
    private Class clazz;
    private Class metamodel;
    private Set<FieldProperty> fields;

    public Entity(Class entity) {
        clazz = entity;
    }

    public String getSimpleName() {
        return clazz.getSimpleName();
    }

    public Set<String> getFieldsNames() {
        if (fields == null) {
            fields = LibraryManager.getEntityFields(this);
        }
        return fields.stream()
                .map(FieldProperty::getName)
                .collect(Collectors.toSet());
    }

    public FieldProperty getFieldProperty(String name) {
        if (fields == null) {
            fields = LibraryManager.getEntityFields(this);
        }
        return fields.stream()
                .filter(fieldProperty -> name.equals(fieldProperty.getName()))
                .findAny()
                .orElse(null);
    }

    public Collection<FieldProperty> getFields() {
        if (fields == null) {
            fields = LibraryManager.getEntityFields(this);
        }
        return fields;
    }

    public Object getIdValue(Object entityObject) {
        if (!entityObject.getClass().equals(clazz)) {
            throw new WrongTypeException(entityObject.getClass(), clazz);
        }
        return fields.stream()
                .filter(FieldProperty::isId)
                .findFirst()
                .map(fieldProperty -> fieldProperty.getValue(entityObject))
                .orElse(null);
    }

}
