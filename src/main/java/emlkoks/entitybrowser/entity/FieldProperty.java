package emlkoks.entitybrowser.entity;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import javax.persistence.Id;

import emlkoks.entitybrowser.session.exception.MethodNotFoundException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;


/**
 * Created by EmlKoks on 17.04.17.
 */
@Data
@Slf4j
public class FieldProperty {
    private String name;
    @Deprecated
    private Method setter;
    @Deprecated
    private Method getter;
    private Field field;
    private EntityDetails owner;

    public FieldProperty(Field field, EntityDetails owner) {
        this.field = field;
        this.field.setAccessible(true);
        this.owner = owner;
        this.name = field.getName();
//        try {
//            setupGetter();
//            setupSetter();
//        } catch (MethodNotFoundException exception) {
//            log.debug("Cannot find method {} in class {}",
//                    exception.getMethodName(), field.getDeclaringClass().getName());
//            throw new CannotCreateFieldPropertyException();
//        }

    }

    public EntityWrapper getValue(EntityWrapper entity) {
        try {
            return new EntityWrapper(field.get(entity.getValue()));
        } catch (IllegalAccessException e) {
            log.error("Cannot get field value", e);
            return null;
        }
    }

    @Deprecated
    public EntityWrapper getValueOld(EntityWrapper entity) {
        try {
            return new EntityWrapper(getter.invoke(entity.getValue()));
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean isId() {
        return field.isAnnotationPresent(Id.class);
    }

    @Deprecated
    private void setupGetter() throws RuntimeException {
        boolean isBoolean = field.getType() == boolean.class;
        String methodName = new StringBuilder()
                .append(isBoolean ? "is" : "get")
                .append(field.getName().substring(0,1).toUpperCase())
                .append(field.getName().substring(1))
                .toString();
        try {
            this.getter = field.getDeclaringClass().getMethod(methodName);
        } catch (NoSuchMethodException e) {
            throw new MethodNotFoundException(methodName);
        }
    }

    @Deprecated
    private void setupSetter() throws RuntimeException {
        String methodName = new StringBuilder()
                .append("set")
                .append(field.getName().substring(0,1).toUpperCase())
                .append(field.getName().substring(1))
                .toString();
        try {
            this.setter = field.getDeclaringClass().getMethod(methodName, field.getType());
        } catch (NoSuchMethodException e) {
            throw new MethodNotFoundException(methodName);
        }
    }

    public boolean isFinal() {
        return Modifier.isFinal(field.getModifiers());
    }
}

