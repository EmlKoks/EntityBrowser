package emlkoks.entitybrowser.session.exception;

import lombok.Data;

@Data
public class MethodNotFoundException extends RuntimeException {
    private String methodName;

    public MethodNotFoundException(String methodName) {
        this.methodName = methodName;
    }
}
