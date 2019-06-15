package emlkoks.entitybrowser.session;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import lombok.Data;

/**
 * Created by EmlKoks on 17.04.17.
 */
@Data
public class FieldProperty {
    String name;
    Method setMethod;
    Method getMethod;
    Field field;
    Class parentClass;

    public FieldProperty(String name) {
        this.name = name;
    }
}

