package emlkoks.entitybrowser.session;

import lombok.Data;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

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
}

