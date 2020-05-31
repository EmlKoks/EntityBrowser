package emlkoks.entitybrowser.session.exception;

public class WrongTypeException extends RuntimeException {

    public WrongTypeException(Class currentClass, Class shouldBeClass) {
        super("Wrong object type. Is " + currentClass.getName()
                + " but should be " + shouldBeClass.getName());
    }
}
