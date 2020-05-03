package emlkoks.entitybrowser.session;

public class WrongTypeException extends RuntimeException {

    public WrongTypeException(Class currentClass, Class shouldBeClass) {
        super("Wrong object type. Is " + currentClass.getName()
                + " but should be " + shouldBeClass.getName());
    }
}
