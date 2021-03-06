package emlkoks.entitybrowser.view.controller.main;

import emlkoks.entitybrowser.Main;
import emlkoks.entitybrowser.Mode;
import emlkoks.entitybrowser.mocked.MockSession;
import emlkoks.entitybrowser.query.FieldFilter;
import emlkoks.entitybrowser.query.SearchResults;
import emlkoks.entitybrowser.query.SearchService;
import emlkoks.entitybrowser.query.comparator.ComparationType;
import emlkoks.entitybrowser.query.comparator.Comparator;
import emlkoks.entitybrowser.query.comparator.ComparatorFactory;
import emlkoks.entitybrowser.session.Session;
import emlkoks.entitybrowser.session.entity.ClassDetails;
import emlkoks.entitybrowser.session.entity.FieldProperty;
import emlkoks.entitybrowser.view.dialog.ErrorDialogCreator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SearchController {
    private final static int NUMBER_OF_CHILDREN_FILTER_GRID = 4;

    @FXML private ChoiceBox<ClassDetails> entities;
    @FXML private ChoiceBox<FieldProperty> fields;
    @FXML private BorderPane filtersPane;
    @FXML private GridPane filtersGrid;

    private MainWindowController parentController;

    private Session session;
    private ClassDetails selectedEntity;
    private ResourceBundle resources;

    private SearchService searchService;

    public void initialize(ResourceBundle resources, MainWindowController parentController, Pane parentPane) {
        this.resources = resources;
        this.parentController = parentController;
        filtersPane.prefWidthProperty().bind(parentPane.widthProperty());
        filtersPane.prefHeightProperty().bind(parentPane.heightProperty());
        entities.valueProperty().addListener((observable, oldValue, newValue) -> {
            selectedEntity = newValue;
            fields.getItems().clear();
            fields.getItems().addAll(selectedEntity.getFields());
            fields.setValue(fields.getItems().get(0));
            filtersGrid.getChildren().clear();
        });
    }

    private Session getSession() {
        return session;
    }

    public void updateSession(Session session) {
        this.session = session;
        searchService = new SearchService(session.getProvider());
        entities.getItems().addAll(session.getClasses());
        if (Mode.DEBUG.equals(Main.mode)) {
            entities.setValue(entities.getItems().get(0));
        }
    }

    @FXML
    public void doSearch() {
        try {
            SearchResults searchResults;
            if (Mode.PROD.equals(Main.mode)) {
                searchResults = searchService.search(selectedEntity, getFieldFilters());
            } else {
                searchResults = MockSession.getSearchResults();
            }
            parentController.getResultsController().showResults(searchResults);
        } catch (Exception e) {
            e.printStackTrace();
            new ErrorDialogCreator("Cannot find results: " + e.getMessage())
                    .show();
        }
    }

    private List<FieldFilter> getFieldFilters()  {
        List<FieldFilter> fieldFilterList = new ArrayList<>();
        ObservableList children = filtersGrid.getChildren();
        for (int i = 0;i < children.size() / NUMBER_OF_CHILDREN_FILTER_GRID;++i) {
            int firstChildInRow = i * NUMBER_OF_CHILDREN_FILTER_GRID;
            FieldProperty fieldProperty = getFieldProperty(children.get(firstChildInRow));
            ComparationType comparationType = getComparationType(children.get(firstChildInRow + 1));
            String value = getFilterValue(children.get(firstChildInRow + 2));
            fieldFilterList.add(new FieldFilter(comparationType, fieldProperty, value));
        }
        return fieldFilterList;
    }

    private FieldProperty getFieldProperty(Object obj) {
        Label label = (Label) obj;
        return selectedEntity.getFieldProperty(label.getText());
    }

    private ComparationType getComparationType(Object obj) {
        ChoiceBox choiceBox = (ChoiceBox) obj;
        return (ComparationType) choiceBox.getValue();
    }

    private String getFilterValue(Object obj) {
        return ((TextField)obj).getText();
    }

    @FXML
    public void addFilter() {
        if (entities.getValue() == null) {
            return;
        }
        List<Node> filterRow = createFilterRow(fields.getValue());
        filtersGrid.addRow(filtersGrid.getChildren().size(), filterRow.toArray(new Node[]{}));
    }

    private List<Node> createFilterRow(FieldProperty field) {
        var comparator = ComparatorFactory.getComparator(field);
        ChoiceBox<ComparationType> comparatorChoiceBox = buildComparationChoiceBox(comparator);
        Node valueField = comparator.createFieldValueField(field.getType());
        comparatorChoiceBox.valueProperty().addListener((observable, oldValue, newValue) ->
                valueField.setDisable(newValue.isNull() || newValue.isNotNull())
        );
        valueField.setDisable(comparatorChoiceBox.getValue().isNull()
                || comparatorChoiceBox.getValue().isNotNull());
        return Arrays.asList(new Label(field.getName()), comparatorChoiceBox, valueField ,createDeleteFilter());
    }

    private ChoiceBox<ComparationType> buildComparationChoiceBox(Comparator comparator) {
        ChoiceBox<ComparationType> choiceBox = new ChoiceBox<>();
        choiceBox.getItems().addAll(comparator.getComparationTypes());
        choiceBox.setValue(choiceBox.getItems().get(0));
        return choiceBox;
    }

    private Label createDeleteFilter() {
        Label remove = new Label("X");//Replace by img
        remove.setTooltip(new Tooltip(resources.getString("search.removeFilter")));
        remove.setTextFill(Color.RED);
        remove.setOnMouseClicked(this::removeFilter);
        return remove;
    }

    private void removeFilter(MouseEvent event) {
        Label source = ((Label)event.getSource());
        int rowIndex = GridPane.getRowIndex(source);
        Set<Node> nodeToRemove = filtersGrid.getChildren().stream()
                .filter(node -> GridPane.getRowIndex(node).equals(rowIndex))
                .collect(Collectors.toSet());

        log.info("Remove filter {}", nodeToRemove);

        filtersGrid.getChildren().removeAll(nodeToRemove);
        filtersGrid.getChildren().stream()
                .filter(node -> GridPane.getRowIndex(node) > rowIndex)
                .forEach(node -> GridPane.setRowIndex(node,
                        GridPane.getRowIndex(node) - NUMBER_OF_CHILDREN_FILTER_GRID));
    }
}
