package emlkoks.entitybrowser.view.controller;

import emlkoks.entitybrowser.session.entity.EntityWrapper;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.stream.IntStream;
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
    private static final int MAX_DEEP = 10;
    private static final int MAX_ITEM_LENGTH = 30;

    @FXML
    private TreeView<Object> tree;

    @FXML
    private ResourceBundle resources;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.resources = resources;
    }

    public void loadEntity(EntityWrapper entity) {
        TreeItem<Object> rootItem = createTreeItem(entity, "Root", 0);
        rootItem.setExpanded(true);
        tree.setRoot(rootItem);
    }

    private TreeItem<Object> createTreeItem(EntityWrapper entity, String fieldName, int deep) {
        log.debug("{} {} {}", IntStream.range(0, deep).mapToObj(x -> "\t").reduce((s1, s2) -> s1 + s2).orElse(""),
                deep, fieldName);
        if (Objects.isNull(entity)) {
            return null;
        }
        if (deep == MAX_DEEP) {
            return new TreeItem<>("...");
        }
        if (entity.isNull() || entity.getEntityDetails().isSimplyType()
                || entity.getEntityDetails().isExcludedType()) {
            return getStringTreeItem(entity, fieldName);
        }
        if (entity.isIterable()) {
            return createIterableItem(entity, fieldName, ++deep);
        }
        return createItemWithChildren(entity, fieldName, ++deep);
    }

    private TreeItem<Object> createIterableItem(EntityWrapper entityWrapper, String fieldName, int deep) {
        TreeItem item = new TreeItem<>("");
        int i = 0;
        for (Object o : (Iterable)entityWrapper.getValue()) {
            TreeItem subitem = createTreeItem(new EntityWrapper(o), "[" + (i++) + "] ", deep);
            if (subitem != null) {
                item.getChildren().add(subitem);
            }
        }
        item.setValue(fieldName + " : " + resources.getString("entityDetails.size") + " = " + i
                + " (" + entityWrapper.getEntityDetails().getFullName() + ")");
        return item;
    }

    private TreeItem<Object> createItemWithChildren(EntityWrapper entity, String fieldName, int deep) {
        TreeItem<Object> item = new TreeItem<>(getTextValue(entity, fieldName));
        entity.getEntityDetails().getFields().stream()
                .filter(fp -> !fp.isFinal())
                .map(fp -> createTreeItem(fp.getValue(entity), fp.getName(), deep))
                .forEach(item.getChildren()::add);
        return item;
    }

    private TreeItem<Object> getStringTreeItem(EntityWrapper entity, String fieldName) {
        return new TreeItem<>(getTextValue(entity, fieldName));
    }

    private TextFlow getTextValue(EntityWrapper entity, String fieldName) {
        TextFlow textFlow = new TextFlow();

        Label labelFieldName = new Label(fieldName);
        labelFieldName.setStyle("-fx-font-weight: bold");

        String stringEntity = entity.getStringValue();
        Label labelValue = new Label(stringEntity.length() > MAX_ITEM_LENGTH
                ? stringEntity.substring(0, MAX_ITEM_LENGTH) + "..." : stringEntity);
        labelValue.setStyle("-fx-font-weight: bold");
        textFlow.getChildren().addAll(labelFieldName, new Label(" = "), labelValue);
        if (!entity.isNull()) {
            Label labelCannonicalName = new Label(" (" + entity.getEntityDetails().getFullName() + ")");
            textFlow.getChildren().add(labelCannonicalName);
        }
        return textFlow;
    }
}
