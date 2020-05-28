package emlkoks.entitybrowser.session.entity;
import javax.persistence.Transient;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EntityFields {
    private List<FieldProperty> fields;
    private ClassDetails ownerClass;

    public EntityFields(ClassDetails ownerClass) {
        this.ownerClass = ownerClass;
    }

    public List<FieldProperty> get() {
        if (fields == null) {
            initFields();
        }
        return fields;
    }

    private void initFields() {
        fields = Stream.concat(
                Stream.of(
                        ownerClass.getClazz().getDeclaredFields()),
                getFieldsFromEntitySuperclass())
                .filter(this::isNotTransient)
                .filter(field -> isNotFinal(field) || ownerClass.isEnum())
                .filter(this::isNotSerialVersionUid)
                .map(FieldProperty::new)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private Stream<Field> getFieldsFromEntitySuperclass() {
        if (ownerClass.isEnum()) {
            return Stream.empty();
        }
        return ownerClass.getSuperEntity()
                .map(ClassDetails::getClazz)
                .map(Class::getDeclaredFields)
                .map(Arrays::stream)
                .orElse(Stream.empty());
    }

    private boolean isNotFinal(Field field) {
        return !Modifier.isFinal(field.getModifiers());
    }

    private boolean isNotTransient(Field field) {
        return Objects.isNull(field.getAnnotation(Transient.class));
    }

    private boolean isNotSerialVersionUid(Field field) {
        return !"serialVersionUID".equals(field.getName());
    }

}
