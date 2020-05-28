package emlkoks.entitybrowser.session.entity;

import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class EntityWrapper {
    public static final Integer MAX_STRING_LENGTH = 30;

    private ClassDetails classDetails;
    private Object value;

    public EntityWrapper(Object value) {
        this.value = value;
        if (!isNull()) {
            classDetails = new ClassDetails(value.getClass());
        }
    }

    public String getStringValue() {
        if (isNull()) {
            return "[null]";
        }
        //TODO String, Date, Number...
        if (value.toString().length() > MAX_STRING_LENGTH) {
            return value.toString().substring(0, MAX_STRING_LENGTH) + "...";
        }
        return value.toString();
    }

    public String createDetailsTitle() {
        if (isNull()) {
            throw new NullPointerException("Cannot create details title. Value is null");
        }
        try {
            return classDetails.getSimpleName() + "(Id: " + classDetails.getIdValue(this) + ")";
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        return classDetails.getSimpleName();
    }

    public boolean isNull() {
        return Objects.isNull(value);
    }

    public boolean isIterable() {
        return value instanceof Iterable;
    }
}
