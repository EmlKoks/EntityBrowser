package emlkoks.entitybrowser.query;

import emlkoks.entitybrowser.query.comparator.expression.Expression;
import emlkoks.entitybrowser.session.entity.FieldProperty;
import lombok.Value;

/**
 * Created by EmlKoks on 19.06.19.
 */
@Value
public class FieldFilter {
    private Expression expression;
    private FieldProperty fieldProperty;
    private String value;
}
