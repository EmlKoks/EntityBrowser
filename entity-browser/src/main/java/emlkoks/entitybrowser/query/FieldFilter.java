package emlkoks.entitybrowser.query;

import emlkoks.entitybrowser.query.comparator.ComparationType;
import emlkoks.entitybrowser.session.entity.FieldProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by EmlKoks on 19.06.19.
 */
@AllArgsConstructor
@Getter
public class FieldFilter {
    private ComparationType comparationType;
    private FieldProperty fieldProperty;
    private Object value;
}
