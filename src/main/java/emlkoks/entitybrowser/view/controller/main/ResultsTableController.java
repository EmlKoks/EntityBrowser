package emlkoks.entitybrowser.view.controller.main;

import emlkoks.entitybrowser.Main;
import emlkoks.entitybrowser.session.Entity;
import emlkoks.entitybrowser.session.FieldProperty;
import emlkoks.entitybrowser.view.ViewFile;
import emlkoks.entitybrowser.view.controller.EntityDetailsController;
import java.io.IOException;
import java.util.List;
import java.util.ResourceBundle;
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


public class ResultsTableController {

    private ResourceBundle resources;
    private Pane parentPane;

    @FXML
    private TableView resultsTable;

    private Entity selectedEntity;

    public void initialize(Pane parentPane, ResourceBundle resources) {
        this.parentPane = parentPane;
        this.resources = resources;
    }

    public void showResults(List<? extends Object> results, Entity entity) {
        this.selectedEntity = entity;
        ObservableList<? extends Object> list = FXCollections.observableList(results);
        createResultsTable(list);
        fillColumns();
        this.resultsTable.setVisible(true);

    }

    private void createResultsTable(ObservableList<? extends Object> results) { //TODO set observable list
        resultsTable.setItems(results);
        resultsTable.prefWidthProperty().bind(parentPane.widthProperty());
        resultsTable.prefHeightProperty().bind(parentPane.heightProperty());
        resultsTable.setRowFactory(v -> {
            TableRow<Object> row = new TableRow<>();
            row.setOnMouseClicked(this::openDetails);
            return row;
        });

    }

    private void fillColumns() {
        selectedEntity.getFields().stream()
                .map(this::createTableColumn)
                .forEach(resultsTable.getColumns()::add);
    }

    private TableColumn<String, Object> createTableColumn(FieldProperty fieldProperty) {
        TableColumn<String, Object> column = new TableColumn<>(fieldProperty.getName());
        column.setCellValueFactory(cellData -> {
            Object cellValue = fieldProperty.getValue(cellData.getValue());
            return new SimpleObjectProperty<>(getStringValue(cellValue));
        });
        return column;
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource(ViewFile.ENTITY_DETAILS.getFile()), resources);
            parent = loader.load();
            EntityDetailsController controller = loader.getController();
            controller.loadEntity(entity);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Cannot open details dialog");//TODO message from lang
        }
        Scene dialogScene = new Scene(parent);
        dialog.setTitle(getDetailsTitle(entity));
        dialog.setScene(dialogScene);
        return dialog;
    }

    private String getDetailsTitle(Object entity) {
        try {
            return selectedEntity.getSimpleName() + "(Id: " + selectedEntity.getIdValue(entity) + ")";
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        return selectedEntity.getSimpleName();
    }
}
