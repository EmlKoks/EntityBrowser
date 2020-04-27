package emlkoks.entitybrowser.common;

import java.lang.reflect.Field;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Util {
    public static final String ENUM_VALUES = "$VALUES";

    public static Enum[] getEnumValues(Class clazz) {
        try {
            Field field = clazz.getDeclaredField(ENUM_VALUES);
            field.setAccessible(true);
            return (Enum[]) field.get(null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            log.error("Cannot load enum " + clazz.getName() + "values", e);
            return null;
        }
    }
}
