package emlkoks.entitybrowser.query.comparator;

/**
 * Created by EmlKoks on 15.06.19.
 */
public class ComparatorNotFoundException extends RuntimeException {

    ComparatorNotFoundException(Class<?> type) {
        super("Not found Comparator for type " + type.getName());
    }
}
