package emlkoks.entitybrowser.session;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javax.persistence.Id;
import lombok.Data;


/**
 * Created by EmlKoks on 17.04.17.
 */
@Data
public class FieldProperty {
    String name;
    Method setter;
    Method getter;
    Field field;
    Class parentClass;

    public FieldProperty(String name) {
        this.name = name;
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
}

