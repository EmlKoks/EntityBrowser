package emlkoks.entitybrowser.query.comparator;

/**
 * Created by EmlKoks on 11.06.19.
 */
public enum ComparationType {
    CONTAINS,
    EQUAL,
    NOT_EQUAL,
    GREATER,
    LESS,
    GREATER_OR_EQUAL,
    LESS_OR_EQUAL,
    BETWEEN,
    IS_NULL,
    IS_NOT_NULL;

    public boolean isNull() {
        return IS_NULL.equals(this);
    }

    public boolean isNotNull() {
        return IS_NOT_NULL.equals(this);
    }
}
