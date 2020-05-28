package emlkoks.entitybrowser.session.entity;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import javax.persistence.Id;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;


/**
 * Created by EmlKoks on 17.04.17.
 */
@Slf4j
public class FieldProperty {
    @Getter
    private String name;
    private Field field;

    public FieldProperty(Field field) {
        this.field = field;
        this.field.setAccessible(true);
        this.name = field.getName();
    }

    public EntityWrapper getValue(EntityWrapper entity) {
        if (entity.isNull()) {
            return new EntityWrapper(null);
        }
        try {
            return new EntityWrapper(field.get(entity.getValue()));
        } catch (IllegalAccessException e) {//Never thrown
            log.error("Cannot get field value", e);
            return new EntityWrapper(null);
        }
    }

    public boolean isId() {
        return hasAnnotation(Id.class);
    }

    public boolean isFinal() {
        return Modifier.isFinal(field.getModifiers());
    }

    public boolean hasAnnotation(Class<? extends Annotation> annotation) {
        return field.isAnnotationPresent(annotation);
    }

    public Class getType() {
        return field.getType();
    }


}

