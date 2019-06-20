package emlkoks.entitybrowser.session;

import java.util.Collection;
import java.util.Set;
import java.util.SortedMap;

/**
 * Created by EmlKoks on 09.04.17.
 */
public class Entity {
    private String name;
    private Class clazz;
    private SortedMap<String, FieldProperty> fields;

    public Entity(Class entity) {
        name = entity.getName();
        clazz = entity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Class getClazz() {
        return clazz;
    }

    public Set<String> getFieldsNames() {
        return fields.keySet();
    }

    public void setFields(SortedMap<String, FieldProperty> fields) {
        this.fields = fields;
    }

    public FieldProperty getField(String name) {
        return fields.get(name);
    }

    public Collection<FieldProperty> getFields() {
        return fields.values();
    }

    public FieldProperty getFieldProperty(String name) {
        return fields.get(name);
    }

}
