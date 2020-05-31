package emlkoks.entitybrowser.session.entity;

import com.google.common.base.Strings;
import emlkoks.entitybrowser.session.exception.ClassCannotBeNullException;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.Entity;
import lombok.Getter;

public class ClassDetails {
    private static final String ENUM_VALUES = "$VALUES";

    @Getter
    protected Class clazz;
    protected EntityFields fields;

    public ClassDetails(Class entity) {
        if (Objects.isNull(entity)) {
            throw new ClassCannotBeNullException("Entity class is null");
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
                .map(fieldProperty -> fieldProperty.getValueOf(entityObject))
                .map(EntityWrapper::getValue)
                .orElse(null);
    }

    public boolean isExcludedType() {
        return clazz == Date.class;
    }

    public boolean isSimplyType() {
        return clazz == String.class
                || clazz.getSuperclass() == Number.class
                || clazz == Boolean.class
                || (isEnum() && isSimpleEnum());
    }

    public boolean isEnum() {
        return clazz.isEnum();
    }

    private boolean isSimpleEnum() {
        return fields.get().stream()
                .filter(field -> !ENUM_VALUES.equals(field.getName()))
                .map(FieldProperty::getType)
                .allMatch(Class::isEnum);
    }

    private boolean hasSuperClass() {
        return clazz.getSuperclass() != Object.class;
    }

    public Optional<ClassDetails> getSuperEntity() {
        return hasSuperClass() ? Optional.of(new ClassDetails(clazz.getSuperclass()))
                : Optional.empty();
    }

    public boolean isEntity() {
        return clazz.getAnnotation(Entity.class) != null;
    }
}
