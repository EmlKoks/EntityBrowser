package emlkoks.entitybrowser.entity;

import emlkoks.entitybrowser.session.exception.CannotCreateFieldPropertyException;

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
    private EntityDetails ownerEntity;

    public EntityFields(EntityDetails ownerEntity) {
        this.ownerEntity = ownerEntity;
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
                        ownerEntity.getClazz().getDeclaredFields()),
                getFieldsFromEntitySuperclass())
                .filter(this::isNotTransient)
                .filter(this::isNotFinal)
                .filter(this::isNotSerialVersionUid)
                .map(field -> {
                    try {
                        return new FieldProperty(field, ownerEntity);
                    } catch (CannotCreateFieldPropertyException e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private Stream<Field> getFieldsFromEntitySuperclass() {
        return ownerEntity.getSuperEntity()
                .map(EntityDetails::getClazz)
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
