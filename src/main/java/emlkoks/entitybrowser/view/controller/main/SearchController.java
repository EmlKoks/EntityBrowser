package emlkoks.entitybrowser.view.controller.main;

import emlkoks.entitybrowser.query.FieldFilter;
import emlkoks.entitybrowser.query.QueryBuilder;
import emlkoks.entitybrowser.query.comparator.AbstractComparator;
import emlkoks.entitybrowser.query.comparator.ComparatorFactory;
import emlkoks.entitybrowser.query.comparator.expression.Expression;
import emlkoks.entitybrowser.session.Entity;
import emlkoks.entitybrowser.session.FieldProperty;
import emlkoks.entitybrowser.session.Session;
import emlkoks.entitybrowser.view.dialog.ErrorDialogCreator;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

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

    public void initialize(MainWindowController parentController, Pane parentPane, Session session) {
        this.parentController = parentController;
        filtersPane.prefWidthProperty().bind(parentPane.widthProperty());
        filtersPane.prefHeightProperty().bind(parentPane.heightProperty());
        this.session = session;
        entities.valueProperty().addListener((observable, oldValue, newValue) -> {
            fields.getItems().clear();
            fields.getItems().addAll(session.getEntity(newValue).getFieldsNames());
            fields.setValue(fields.getItems().get(0));
            filtersGrid.getChildren().clear();
            addedFilters.clear();
        });
    }

    public void updateEntities(Session session) {
        this.session = session;
        entities.getItems().addAll(session.getClassNames());
    }

    @FXML
    public void doSearch() {
        Entity entity = session.getEntity(entities.getValue());
        QueryBuilder pc = new QueryBuilder(session.getCriteriaBuilder(), entity, prepareFieldFilters());
        try {
            List resultList = session.find(pc);
            parentController.getResultsController().showResults(resultList, entity);
        } catch (Exception e) {
            e.printStackTrace();
            Throwable t = e;
            while (t.getCause() != null) {
                t = t.getCause();
            }
            new ErrorDialogCreator(t.getMessage())
                    .show();
        }
    }

    private List<FieldFilter> prepareFieldFilters()  {
        Entity entity = session.getEntity(entities.getValue());
        List<FieldFilter> fieldFilterList = new ArrayList<>();
        ObservableList children = filtersGrid.getChildren();
        for (int i = 0;i < addedFilters.size();++i) {
            FieldProperty fieldProperty = entity.getFieldProperty(((Label)children.get(i * 2)).getText());
            Expression expression = (Expression) ((ChoiceBox)children.get(i * 3 + 1)).getValue();
            String value = ((TextField)children.get(i * 3 + 2)).getText();
            fieldFilterList.add(new FieldFilter(expression, fieldProperty, value));
        }
        return fieldFilterList;
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
