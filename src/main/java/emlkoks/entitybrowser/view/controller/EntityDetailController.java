package emlkoks.entitybrowser.view.controller;

import emlkoks.entitybrowser.Main;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

/**
 * Created by EmlKoks on 01.05.17.
 */
public class EntityDetailController implements Initializable {
    private static final int MAX_DEEP = 20;
    private static final int MAX_ITEM_LENGTH = 30;

    @FXML
    private TreeView<Object> tree;

    @FXML
    private ResourceBundle resources;

    private int deep;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.resources = resources;
        Object entity = Main.getEntity();
        if (entity != null) {
            loadEntity(entity);
        } else {
            //TODO throw error?
        }
    }

    private void loadEntity(Object entity) {
        deep = 0;
        TreeItem<Object> rootItem = createTreeItem(entity, "Root");
        rootItem.setExpanded(true);
        tree.setRoot(rootItem);
//        tree.setCellFactory(tv -> {
//            TreeCell<Object> cell = new TreeCell<Object>() {
//                @Override
//                protected void updateItem(Object item, boolean empty) {
//                    String value = entity.toString();
//                    String svalue = value.length()>20 ? value.substring(0,20)+"..." : value;
//                    if (empty) {
//                        setText(null);
//                        setTooltip(null);
//                    } else if (getTreeItem() == rootItem) {
//                        setText("Root:" + svalue + " (" + item.getClass().getCanonicalName() + ")");
//                        setTooltip(new Tooltip(value));
//                    } else {
//                        setText("Root:" + svalue + " (" + item.getClass().getCanonicalName() + ")");
//                        setTooltip(new Tooltip(value));
//                    }
//                }
//            };
//            return cell;
//        });
    }

    private TreeItem<Object> createTreeItem(Object entity, String fieldName) {
        System.out.println(deep + " " + fieldName);
        ++deep;
        if (deep == MAX_DEEP) {
            --deep;
            return new TreeItem<>("...");
        }
        if (entity == null) {
            --deep;
            return null;
        }
        if (isSimplyType(entity.getClass())) {
            --deep;
            return getStringValue(entity, fieldName);
        }
        if (entity instanceof Iterable) {
            --deep;
            return createIterableItem((Iterable) entity, fieldName);
        }
        return createItemWithChildren(entity, fieldName);
    }

    private boolean isSimplyType(Class entityClass) {
        return entityClass.isEnum() || entityClass == String.class || entityClass.getSuperclass() == Number.class
                || entityClass == Boolean.class;
    }

    private TreeItem<Object> createIterableItem(Iterable entity, String fieldName) {
        TreeItem item = new TreeItem<>("");
        int i = 0;
        for (Object o : entity) {
            TreeItem subitem = createTreeItem(o, "[" + (i++) + "] ");
            if (subitem != null) {
                item.getChildren().add(subitem);
            }
        }
        item.setValue(fieldName + " : size = " + i + " (" + entity.getClass().getCanonicalName() + ")");
        return item;
    }

    private TreeItem<Object> createItemWithChildren(Object entity, String fieldName) {
        String toStringValue = entity.toString();
        TreeItem<Object> item = new TreeItem<>(fieldName + ": "
                + (toStringValue.length() > MAX_ITEM_LENGTH ? toStringValue.substring(0,MAX_ITEM_LENGTH)
                + "..." : toStringValue) + " (" + entity.getClass().getCanonicalName() + ")");
        Field[] fields = entity.getClass().getDeclaredFields();
        for (Field f : fields) {
            if (Modifier.isFinal(f.getModifiers())) {
                continue;
            }
            f.setAccessible(true);
            try {
                TreeItem subitem = createTreeItem(f.get(entity), f.getName());
                if (subitem != null) {
                    item.getChildren().add(subitem);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        deep--;
        return item;
    }

    private TreeItem<Object> getStringValue(Object entity, String fieldName) {
        String toStringValue = entity.toString();
        return new TreeItem<>(fieldName + " = "
                + (toStringValue.length() > MAX_ITEM_LENGTH ? toStringValue.substring(0,MAX_ITEM_LENGTH)
                + "..." : toStringValue) + " (" + entity.getClass().getCanonicalName() + ")");
    }

}
