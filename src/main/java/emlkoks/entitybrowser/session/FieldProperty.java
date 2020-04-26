package emlkoks.entitybrowser.session;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javax.persistence.Id;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;


/**
 * Created by EmlKoks on 17.04.17.
 */
@Data
@Slf4j
public class FieldProperty {
    String name;
    Method setter;
    Method getter;
    Field field;
    Class parentClass;

    public FieldProperty(Field field, Class parentClass) {
        this.field = field;
        this.parentClass = parentClass;
        this.name = field.getName();
        try {
            setupGetter();
            setupSetter();
        } catch (MethodNotFoundException exception) {
            log.debug("Cannot find method {} in class {}", exception.getMethodName(), field.getDeclaringClass().getName());
            throw new CannotCreateFieldPropertyException();
        }

    }

    public Object getValue(Object object) {
        try {
            return getter.invoke(object);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean isId() {
        return field.isAnnotationPresent(Id.class);
    }

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
}

