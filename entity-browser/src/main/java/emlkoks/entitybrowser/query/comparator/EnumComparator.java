package emlkoks.entitybrowser.query.comparator;

import emlkoks.entitybrowser.query.FieldFilter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;

/**
 * Created by EmlKoks on 15.06.19.
 */
public class EnumComparator extends Comparator {

    EnumComparator() {
        comparationTypes.add(ComparationType.EQUAL);
        comparationTypes.add(ComparationType.NOT_EQUAL);
    }

    @Override
    public boolean canUseForClass(Class<?> clazz) {
        return clazz.isEnum();
    }

    @Override
    protected Node createFieldValueField(Class<?> clazz) {
        return new ChoiceBox<>(createValueList(clazz));
    }

    public ObservableList<Enum> createValueList(Class<?> clazz) {
        ObservableList<Enum> values = FXCollections.observableArrayList();
//        values.addAll(Util.getEnumValues(clazz)); TODO
        return values;
    }

    @Override
    public Predicate createPredicate(CriteriaBuilder cb, Path attributePath, FieldFilter fieldFilter) {
        return null;//TODO
    }


}
