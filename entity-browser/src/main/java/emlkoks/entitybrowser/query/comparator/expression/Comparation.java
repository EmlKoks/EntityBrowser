package emlkoks.entitybrowser.query.comparator.expression;

import emlkoks.entitybrowser.Main;

/**
 * Created by EmlKoks on 11.06.19.
 */
public abstract class Comparation {
    private ComparationType comparationType;
    private String label;

    Comparation(ComparationType comparationType) {
        this.comparationType = comparationType;
        this.label = Main.resources.getString("expression." + comparationType.name().toLowerCase());
    }

    public String toString() {
        return label;
    }

    public ComparationType getType() {
        return comparationType;
    }
}

