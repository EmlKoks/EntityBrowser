package emlkoks.entitybrowser.query.comparator;

public class ComparationTypeNotAllowedException extends RuntimeException {

    public ComparationTypeNotAllowedException(ComparationType comparationType, Comparator comparator) {
        super("Comparation " + comparationType + " not allowed for comparator " + comparator.getClass());
    }
}
