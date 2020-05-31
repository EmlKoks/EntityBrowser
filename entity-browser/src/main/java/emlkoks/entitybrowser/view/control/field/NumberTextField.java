package emlkoks.entitybrowser.view.control.field;

import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NumberTextField extends TextField {
    private Class<?> type;

    public NumberTextField(Class<?> type) {
        this.type = type;
        this.setTextFormatter(new TextFormatter<Object>(change -> {
                if (change.isAdded() && !matchValue(change.getControlText() + change.getText())) {
                    change.setText("");
                }
                return change;
            }));
    }

    private boolean matchValue(String value) {
        if (Integer.class.isAssignableFrom(type) || type == int.class) {
            return value.matches("[0-9]+");
        } else if (Float.class.isAssignableFrom(type) || Double.class.isAssignableFrom(type)
                || type == float.class || type == double.class) {
            return value.matches("[0-9]+") || value.matches("[0-9]+[.,][0-9]*");
        }
        log.error("Field type {} is not matched", type);
        return false;
    }
}
