package emlkoks.entitybrowser.query;

import emlkoks.entitybrowser.query.comparator.ComparationType;
import emlkoks.entitybrowser.session.entity.FieldProperty;
import java.util.Objects;
import lombok.Getter;

/**
 * Created by EmlKoks on 19.06.19.
 */
@Getter
public class FieldFilter {
    private ComparationType comparationType;
    private FieldProperty fieldProperty;
    private Object[] values;

    public FieldFilter(ComparationType comparationType, FieldProperty fieldProperty, Object... values) {
        this.comparationType = comparationType;
        this.fieldProperty = fieldProperty;
        this.values = values;
    }

    public Object getValue() {
        if (Objects.isNull(values)) {
            return null;
        }
        return values[0];
    }

    public Object getValue(int valueIndex) {
        if (Objects.isNull(values) || valueIndex >= values.length) {
            return null;
        }
        return values[valueIndex];
    }
}
