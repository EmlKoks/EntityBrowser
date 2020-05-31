package emlkoks.entitybrowser.session.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ClassCannotBeNullException extends NullPointerException {

    public ClassCannotBeNullException(String message) {
        super(message);
    }
}
