package emlkoks.entitybrowser.session;

/**
 * Created by EmlKoks on 11.08.19.
 */
public class CreateSessionException extends RuntimeException {

    public CreateSessionException(Error error) {
        super(error.getMessage(), error.getCause());
    }
}
