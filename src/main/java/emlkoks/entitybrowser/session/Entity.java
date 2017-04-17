package emlkoks.entitybrowser.session;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Created by EmlKoks on 09.04.17.
 */
public class Entity {
    private String name;
    private Class clazz;
    private SortedMap<String, FieldProperty> fields = new TreeMap<>();

    public Entity(Class entity){
        name = entity.getName();
        clazz = entity;
        loadFields();
    }
    
    private void loadFields(){
        for(Field field : clazz.getDeclaredFields()){
            FieldProperty fp = new FieldProperty(field.getName());
            fp.setField(field);
            fp.setParentClass(clazz);
            String methodName = field.getName().substring(0,1).toUpperCase() + field.getName().substring(1);
            try {
                Method getMethod = clazz.getMethod("get" + methodName);
                fp.setGetMethod(getMethod);
            } catch(NoSuchMethodException e){
                e.printStackTrace();
            }
            try {
                Method setMethod = clazz.getMethod("set" + methodName);
                fp.setSetMethod(setMethod);
            } catch(NoSuchMethodException e){
                e.printStackTrace();
            }
            fields.put(field.getName(), fp);

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

    public Set<String> getFieldsNames() {
        return fields.keySet();
    }

    public Field getField(String name){
        return fields.get(name).getField();
    }

    public Collection<FieldProperty> getFields(){
        return fields.values();
    }

    public FieldProperty getFieldProperty(String name){
        return fields.get(name);
    }

}
