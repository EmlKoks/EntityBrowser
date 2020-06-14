package emlkoks.entitybrowser.view.controller.main;

import emlkoks.entitybrowser.Main;
import emlkoks.entitybrowser.Mode;
import emlkoks.entitybrowser.query.SearchResults;
import emlkoks.entitybrowser.session.entity.ClassDetails;
import emlkoks.entitybrowser.session.entity.EntityWrapper;
import emlkoks.entitybrowser.session.entity.FieldProperty;
import emlkoks.entitybrowser.view.CannotOpenStageException;
import emlkoks.entitybrowser.view.ViewFile;
import emlkoks.entitybrowser.view.controller.EntityDetailsController;
import emlkoks.entitybrowser.view.dialog.ErrorDialogCreator;
import java.io.IOException;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleObjectProperty;
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

    public void initialize(ResourceBundle resources, Pane parentPane) {
        this.parentPane = parentPane;
        this.resources = resources;
    }

    public void showResults(SearchResults searchResults) {
        createResultsTable(searchResults.getResults());
        fillColumns(searchResults.getClassDetails());
        this.resultsTable.setVisible(true);
        if (Mode.DEBUG.equals(Main.mode)) {
//            openDetails(connections.get(0));
        }
    }

    private void createResultsTable(ObservableList<EntityWrapper> results) {
        resultsTable.setItems(results);
        resultsTable.prefWidthProperty().bind(parentPane.widthProperty());
        resultsTable.prefHeightProperty().bind(parentPane.heightProperty());
        resultsTable.setRowFactory(v -> {
            TableRow<Object> row = new TableRow<>();
            row.setOnMouseClicked(this::handleClickEvent);
            return row;
        });

    }

    private void fillColumns(ClassDetails classDetails) {
        classDetails.getFields().stream()
                .map(this::createTableColumn)
                .forEach(resultsTable.getColumns()::add);
    }

    private TableColumn<EntityWrapper, String> createTableColumn(FieldProperty fieldProperty) {
        TableColumn<EntityWrapper, String> column = new TableColumn<>(fieldProperty.getName());
        column.setCellValueFactory(cellData -> {
            EntityWrapper cellValue = fieldProperty.getValueOf(cellData.getValue());
            return new SimpleObjectProperty<>(cellValue.getStringValue());
        });
        return column;
    }

    private void handleClickEvent(MouseEvent event) {
        EntityWrapper selectedItem = (EntityWrapper) ((TableRow)event.getSource()).getItem();
        openDetails(selectedItem);
    }

    private void openDetails(EntityWrapper item) {
        try {
            Stage dialog = createDetailsDialog(item);
            dialog.show();
        } catch (CannotOpenStageException exception) {
            new ErrorDialogCreator("Cannot open details dialog")//TODO get from lang
                    .show();
        }
    }

    private Stage createDetailsDialog(EntityWrapper entity) throws CannotOpenStageException {
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
            throw new CannotOpenStageException();
        }
        Scene dialogScene = new Scene(parent);
        dialog.setTitle(entity.createDetailsTitle());
        dialog.setScene(dialogScene);
        return dialog;
    }
}
