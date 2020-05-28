package emlkoks.entitybrowser.session.entity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.common.base.Strings;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by EmlKoks on 09.04.17.
 */
@Slf4j
public class EntityDetails {
    public static final String ENUM_VALUES = "$VALUES";
    @Getter
    private Class clazz;
    private EntityFields fields;

    public EntityDetails(Class entity) {
        if (Objects.isNull(entity)) {
            throw new NullPointerException("Entity class is null");
        }
        clazz = entity;
        fields = new EntityFields(this);
    }

    public List<FieldProperty> getFields() {
        return fields.get();
    }

    public String getSimpleName() {
        return clazz.getSimpleName();
    }

    public String getFullName() {
        return clazz.getCanonicalName();
    }

    public Set<String> getFieldsNames() {
        return fields.get().stream()
                .map(FieldProperty::getName)
                .collect(Collectors.toSet());
    }

    public FieldProperty getFieldProperty(String name) {
        if (Strings.isNullOrEmpty(name)) {
            throw new NullPointerException("Name cannot be null or empty");
        }
        return fields.get().stream()
                .filter(fieldProperty -> fieldProperty.getName().equals(name))
                .findAny()
                .orElse(null);
    }

    public Object getIdValue(EntityWrapper entityObject) {
        return fields.get().stream()
                .filter(FieldProperty::isId)
                .findFirst()
                .map(fieldProperty -> fieldProperty.getValue(entityObject))
                .orElse(null);
    }

    private boolean hasSuperClass() {
        return clazz.getSuperclass() != Object.class;
    }

    public Optional<EntityDetails> getSuperEntity() {
        return hasSuperClass() ? Optional.of(new EntityDetails(clazz.getSuperclass()))
                : Optional.empty();
    }

    public boolean isSimplyType() {
        return clazz == String.class
                || clazz.getSuperclass() == Number.class
                || clazz == Boolean.class
                || isSimplyEnum();
    }

    private boolean isSimplyEnum() {
        if (!clazz.isEnum()) {
            return false;
        }
        List<Enum> enumValues = getEnumValues();
        return Stream.of(clazz.getDeclaredFields())
                .filter(field -> {
                    try {
                        return !enumValues.contains(getFieldObject(field));
                    } catch (NullPointerException e) {
                        log.debug("Enum values not contain field", e);
                        return true;
                    }
                })
                .noneMatch(field -> !ENUM_VALUES.equals(field.getName()));
    }

    private Object getFieldObject(Field field) throws NullPointerException {
        try {
            field.setAccessible(true);//TODO move other place
            return field.get(null);
        } catch (IllegalAccessException e) {
            log.debug("Cannot get field object", e);
            return null;
        }
    }

    public boolean isExcludedType() {
        return clazz == Date.class;
    }

    private List<Enum> getEnumValues() {
        try {
            Field field = clazz.getDeclaredField(ENUM_VALUES);
            field.setAccessible(true);
            return Arrays.asList((Enum[]) field.get(null));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            log.error("Cannot load enum " + clazz.getName() + "values", e);
            return new ArrayList<>();
        }
    }

}
