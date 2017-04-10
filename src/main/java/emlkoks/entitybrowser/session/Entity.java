package emlkoks.entitybrowser.session;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by EmlKoks on 09.04.17.
 */
public class Entity {
    private String name;
    private Class clazz;
    private Map<String, Field> fields = new HashMap<>();

    public Entity(Class entity){
        name = entity.getName();
        clazz = entity;
        loadFields();
    }
    
    private void loadFields(){
        for(Field field : clazz.getDeclaredFields()){
            fields.put(field.getName(), field);
        }
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

    public Map<String, Field> getFields() {
        return fields;
    }

}
