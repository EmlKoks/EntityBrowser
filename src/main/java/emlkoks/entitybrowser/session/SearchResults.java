package emlkoks.entitybrowser.session;

import emlkoks.entitybrowser.session.entity.EntityDetails;
import emlkoks.entitybrowser.session.entity.EntityWrapper;
import java.util.List;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;


@Getter
public class SearchResults {
    private ObservableList<EntityWrapper> results;
    private EntityDetails entityDetails;

    public SearchResults(EntityDetails entityDetails, List<Object> resultsList) {
        this.entityDetails = entityDetails;
        results = resultsList.stream()
                .map(o -> new EntityWrapper(entityDetails, o))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
    }
}
