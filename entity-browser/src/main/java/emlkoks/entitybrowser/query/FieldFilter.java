package emlkoks.entitybrowser.query;

import emlkoks.entitybrowser.query.comparator.comparation.Comparation;
import emlkoks.entitybrowser.session.entity.FieldProperty;
import lombok.Value;

/**
 * Created by EmlKoks on 19.06.19.
 */
@Value
public class FieldFilter {
    Comparation comparation;
    FieldProperty fieldProperty;
    String value;
}
