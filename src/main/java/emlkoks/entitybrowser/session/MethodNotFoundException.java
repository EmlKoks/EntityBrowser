package emlkoks.entitybrowser.session;

import lombok.Data;

@Data
public class MethodNotFoundException extends RuntimeException {
    private String methodName;
    public MethodNotFoundException(String methodName) {
        this.methodName = methodName;
    }
}
