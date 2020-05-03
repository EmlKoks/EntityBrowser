package emlkoks.entitybrowser.view.controller;

import emlkoks.entitybrowser.common.Util;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.stream.Stream;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.text.TextFlow;
import lombok.extern.slf4j.Slf4j;


/**
 * Created by EmlKoks on 01.05.17.
 */
@Slf4j
public class EntityDetailsController implements Initializable {
    private static final int MAX_DEEP = 20;
    private static final int MAX_ITEM_LENGTH = 30;

    @FXML
    private TreeView<Object> tree;

    @FXML
    private ResourceBundle resources;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.resources = resources;
    }

    public void loadEntity(Object entity) {
        TreeItem<Object> rootItem = createTreeItem(entity, "Root", 0);
        rootItem.setExpanded(true);
        tree.setRoot(rootItem);
    }

    private TreeItem<Object> createTreeItem(Object entity, String fieldName, int deep) {
        log.debug("{} {}", deep, fieldName);
        if (Objects.isNull(entity)) {
            return null;
        }
        if (deep == MAX_DEEP) {
            return new TreeItem<>("...");
        }
        if (isSimplyType(entity.getClass())) {
            return getStringTreeItem(entity, fieldName);
        }
        if (entity instanceof Iterable) {
            return createIterableItem((Iterable) entity, fieldName, ++deep);
        }
        return createItemWithChildren(entity, fieldName, ++deep);
    }

    private TreeItem<Object> createIterableItem(Iterable entity, String fieldName, int deep) {
        TreeItem item = new TreeItem<>("");
        int i = 0;
        for (Object o : entity) {
            TreeItem subitem = createTreeItem(o, "[" + (i++) + "] ", deep);
            if (subitem != null) {
                item.getChildren().add(subitem);
            }
        }
        item.setValue(fieldName + " : " + resources.getString("entityDetails.size") + " = " + i
                + " (" + entity.getClass().getCanonicalName() + ")");
        return item;
    }

    private TreeItem<Object> createItemWithChildren(Object entity, String fieldName, int deep) {
        TreeItem<Object> item = new TreeItem<>(getTextValue(entity, fieldName));
        Stream.of(entity.getClass().getDeclaredFields())
                .filter(field -> !Modifier.isFinal(field.getModifiers()))
                .peek(field -> field.setAccessible(true))
                .map(field -> {
                    return createTreeItem(getFieldObject(field, entity), field.getName(), deep);
                })
                .forEach(item.getChildren()::add);
        return item;
    }

    private TreeItem<Object> getStringTreeItem(Object entity, String fieldName) {
        return new TreeItem<>(getTextValue(entity, fieldName));
    }

    private TextFlow getTextValue(Object entity, String fieldName) {
        TextFlow textFlow = new TextFlow();

        Label labelFieldName = new Label(fieldName);
        labelFieldName.setStyle("-fx-font-weight: bold");

        String stringEntity = entity.toString();
        Label labelValue = new Label(stringEntity.length() > MAX_ITEM_LENGTH
                ? stringEntity.substring(0, MAX_ITEM_LENGTH) + "..." : stringEntity);
        labelValue.setStyle("-fx-font-weight: bold");

        Label labelCannonicalName = new Label(" (" + entity.getClass().getCanonicalName() + ")");

        textFlow.getChildren().addAll(labelFieldName, new Label(" = "), labelValue, labelCannonicalName);
        return textFlow;
    }

    private boolean isSimplyType(Class propertyClass) {
        return propertyClass == String.class
                || propertyClass.getSuperclass() == Number.class
                || propertyClass == Boolean.class
                || isSimplyEnum(propertyClass);
    }

    private boolean isSimplyEnum(Class propertyClass) {
        if (!propertyClass.isEnum()) {
            return false;
        }
        List<Enum> enumValues = Arrays.asList(Util.getEnumValues(propertyClass));
        return Stream.of(propertyClass.getDeclaredFields())
                .filter(field -> {
                    try {
                        return !enumValues.contains(getFieldObject(field, null));
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                        return true;
                    }
                })
                .noneMatch(field -> !Util.ENUM_VALUES.equals(field.getName()));
    }

    private Object getFieldObject(Field field, Object object) {
        try {
            return field.get(object);
        } catch (IllegalAccessException | NullPointerException e) {
            e.printStackTrace();
            return null;
        }
    }
}
