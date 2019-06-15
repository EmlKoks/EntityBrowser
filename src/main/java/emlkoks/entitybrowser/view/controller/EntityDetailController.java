package emlkoks.entitybrowser.view.controller;

import emlkoks.entitybrowser.Main;
import java.lang.reflect.Field;
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

    private TreeItem createTreeItem(Object entity, String name) {
        ++deep;
        if (deep == 20) {
            --deep;
            return new TreeItem("...");
        }
        TreeItem<Object> item;
        String value = entity.toString();
        if (!entity.getClass().isEnum() && entity.getClass() != String.class
                && entity.getClass().getSuperclass() != Number.class && entity.getClass() != Boolean.class) {
            if (entity instanceof Iterable) {
                item = new TreeItem<>("");
                int i = 0;
                for (Object o : (Iterable) entity) {
                    item.getChildren().add(createTreeItem(o, "[" + (i++) + "] "));
                }
                item.setValue(name + " : size = " + i + " (" + entity.getClass().getCanonicalName() + ")");
            } else {
                item = new TreeItem<>(name + ": "
                        + (value.length() > 30 ? value.substring(0,30) + "..." : value)
                        + " (" + entity.getClass().getCanonicalName() + ")");
                Field[] fields = entity.getClass().getDeclaredFields();
                for (Field f : fields) {
                    f.setAccessible(true);
                    try {
                        item.getChildren().add(createTreeItem(f.get(entity), f.getName()));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            item = new TreeItem<>(name + " = "
                    + (value.length() > 30 ? value.substring(0,30) + "..." : value)
                    + " (" + entity.getClass().getCanonicalName() + ")");
        }
        --deep;
        return item;
    }

}
