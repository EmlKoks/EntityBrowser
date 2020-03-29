package emlkoks.entitybrowser.view.controller.main;

import emlkoks.entitybrowser.Main;
import emlkoks.entitybrowser.session.Entity;
import emlkoks.entitybrowser.session.FieldProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.persistence.Id;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class ResultsTableController {

    private ResourceBundle resources;
    private Pane parentPane;

    @FXML
    private TableView resultsTable;

    public void initialize(Pane parentPane, ResourceBundle resources) {
        this.parentPane = parentPane;
        this.resources = resources;
    }

    public void showResults(List<? extends Object> results, Entity entity) {
        ObservableList<? extends Object> list = FXCollections.observableList(results);
        createResultsTable(list);
        for (FieldProperty fp : entity.getFields()) {
            TableColumn column = new TableColumn(fp.getName());
            column.setCellValueFactory(cellData -> {
                try {
                    Object cellValue = fp.getGetter().invoke(((TableColumn.CellDataFeatures) cellData).getValue());
                    return new SimpleObjectProperty<>(getStringValue(cellValue));
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
                return null;
            });
            resultsTable.getColumns().add(column);
        }
    }

    private String getStringValue(Object obj) {
        //TODO String, Date, Number...
        if (obj == null) {
            return null;
        }
        if (obj.toString().length() > 30) {
            return "{" + obj.getClass().getSimpleName() + "}";
        }
        return obj.toString();
    }

    private void createResultsTable(ObservableList<? extends Object> results) {//TODO set observable list
        resultsTable.setItems(results);
        resultsTable.prefWidthProperty().bind(parentPane.widthProperty());
        resultsTable.prefHeightProperty().bind(parentPane.heightProperty());
        resultsTable.setRowFactory(v -> {
            TableRow<Object> row = new TableRow<>();
            row.setOnMouseClicked(this::openDetails);
            return row;
        });
    }

    private void openDetails(MouseEvent event) {
        Object selectedItem = ((TableRow)event.getSource()).getItem();
        Main.addEntity(selectedItem);
        Stage dialog = createDietailsDialog(selectedItem);
        dialog.show();
    }

    private Stage createDietailsDialog(Object entity) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.NONE);
        dialog.initOwner(parentPane.getScene().getWindow());
        Parent parent;
        try {
            parent = FXMLLoader.load(getClass().getResource("/view/entityDetails.fxml"), resources);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Cannot open details dialog");//TODO message from lang
        }
        Scene dialogScene = new Scene(parent);
        dialog.setTitle(buildTitle(entity));
        dialog.setScene(dialogScene);
        return dialog;
    }

    private String buildTitle(Object entity) {
        Arrays.asList(entity.getClass().getDeclaredFields()).stream()
                .filter(field -> field.isAnnotationPresent(Id.class))
                .collect(Collectors.toList());
//        selectedItem.getClass().getSimpleName()
        return entity.getClass().getSimpleName() + "(Id: " + 0 + ")";
    }
}
