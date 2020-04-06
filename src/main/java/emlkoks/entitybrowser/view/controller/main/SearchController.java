package emlkoks.entitybrowser.view.controller.main;

import emlkoks.entitybrowser.Main;
import emlkoks.entitybrowser.Mode;
import emlkoks.entitybrowser.mocked.MockSession;
import emlkoks.entitybrowser.query.FieldFilter;
import emlkoks.entitybrowser.query.comparator.AbstractComparator;
import emlkoks.entitybrowser.query.comparator.ComparatorFactory;
import emlkoks.entitybrowser.query.comparator.expression.Expression;
import emlkoks.entitybrowser.session.Entity;
import emlkoks.entitybrowser.session.FieldProperty;
import emlkoks.entitybrowser.session.Session;
import emlkoks.entitybrowser.view.dialog.ErrorDialogCreator;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;


public class SearchController {

    @FXML
    private ChoiceBox<String> entities;

    @FXML
    private ChoiceBox<String> fields;

    @FXML
    private BorderPane filtersPane;

    @FXML
    private GridPane filtersGrid;

    private MainWindowController parentController;

    private Session session;
    private Set<String> addedFilters = new TreeSet<>();
    private Entity selectedEntity;

    public void initialize(MainWindowController parentController, Pane parentPane, Session session) {
        this.parentController = parentController;
        filtersPane.prefWidthProperty().bind(parentPane.widthProperty());
        filtersPane.prefHeightProperty().bind(parentPane.heightProperty());
        this.session = session;
        entities.valueProperty().addListener((observable, oldValue, newValue) -> {
            selectedEntity = session.getEntity(newValue);
            fields.getItems().clear();
            fields.getItems().addAll(selectedEntity.getFieldsNames());
            fields.setValue(fields.getItems().get(0));
            filtersGrid.getChildren().clear();
            addedFilters.clear();
        });
    }

    public void updateEntities(Session session) {
        this.session = session;
        entities.getItems().addAll(session.getClassNames());
        if (Mode.DEBUG.equals(Main.mode)) {
            entities.setValue(entities.getItems().get(0));
        }
    }

    @FXML
    public void doSearch() {
        try {
//            List resultList = SearchService.searchResults(session, selectedEntity, getFieldFilters());
            List resultList = MockSession.getResultsList();
            parentController.getResultsController().showResults(resultList, selectedEntity);
        } catch (Exception e) {
            e.printStackTrace();
            new ErrorDialogCreator("Cannot find results")
                    .show();
        }
    }

    private List<FieldFilter> getFieldFilters()  {
        List<FieldFilter> fieldFilterList = new ArrayList<>();
        ObservableList children = filtersGrid.getChildren();
        for (int i = 0;i < addedFilters.size();++i) {
            int firstChildInRow = i * 3;
            FieldProperty fieldProperty = getFieldProperty(children.get(firstChildInRow));
            Expression expression = getExpression(children.get(firstChildInRow + 1));
            String value = getFilterValue(children.get(firstChildInRow + 2));
            fieldFilterList.add(new FieldFilter(expression, fieldProperty, value));
        }
        return fieldFilterList;
    }

    private FieldProperty getFieldProperty(Object obj) {
        Label label = (Label) obj;
        return selectedEntity.getFieldProperty(label.getText());
    }

    private Expression getExpression(Object obj) {
        ChoiceBox choiceBox = (ChoiceBox) obj;
        return (Expression) choiceBox.getValue();
    }

    private String getFilterValue(Object obj) {
        return ((TextField)obj).getText();
    }

    @FXML
    public void addFilter() {
        if (entities.getValue() == null) {
            return;
        }
        String fieldName = fields.getValue();
        if (addedFilters.contains(fieldName)) {
            return;//Can add twice the same field?
        }
        FieldProperty field = session.getEntity(entities.getValue()).getField(fieldName);
        AbstractComparator comparator = ComparatorFactory.getComparator(field);
        filtersGrid.addRow(filtersGrid.getChildren().size(), comparator.createFilterRow(field));
        addedFilters.add(fieldName);
    }

}
