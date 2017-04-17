package emlkoks.entitybrowser.session;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by EmlKoks on 17.04.17.
 */
public class FieldProperty {
    String name;
    Method setMethod;
    Method getMethod;
    Field field;
    Class parentClass;

    public FieldProperty(String name){
        this.name = name;
    }

    public Method getSetMethod() {
        return setMethod;
    }

    public void setSetMethod(Method setMethod) {
        this.setMethod = setMethod;
    }

    public Method getGetMethod() {
        return getMethod;
    }

    public void setGetMethod(Method getMethod) {
        this.getMethod = getMethod;
    }

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Class getParentClass() {
        return parentClass;
    }

    public void setParentClass(Class parentClass) {
        this.parentClass = parentClass;
    }
}

