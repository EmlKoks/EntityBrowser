package emlkoks.entitybrowser.query.comparator;

public class ComparationTypeNotImplementedException extends RuntimeException {

    public ComparationTypeNotImplementedException(ComparationType comparationType) {
        super("Comparation " + comparationType + " not allowed");
    }
}
