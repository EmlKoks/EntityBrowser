package emlkoks.entitybrowser.view.control;

import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;

import java.util.function.UnaryOperator;

public class NumberTextField extends TextField {
    private Class type;

    public NumberTextField(Class type) {
        this.type = type;
        this.setTextFormatter(new TextFormatter<Object>(new UnaryOperator<TextFormatter.Change>() {
            @Override
            public TextFormatter.Change apply(TextFormatter.Change change) {
                if (change.isAdded()) {
                    if(change.getText().matches("[^0-9]")) {//TODO add decimal types handling
                        change.setText("");
                    }
                }
                return change;
            }
        }));
    }
}
