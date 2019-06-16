package emlkoks.entitybrowser.query.comparator;

import emlkoks.entitybrowser.query.comparator.expression.EqualExpression;
import emlkoks.entitybrowser.query.comparator.expression.NotEqualExpression;

/**
 * Created by EmlKoks on 15.06.19.
 */
public class CharacterComparator extends AbstractComparator {

    public CharacterComparator() {
        expressions.add(new EqualExpression());
        expressions.add(new NotEqualExpression());
    }
}
