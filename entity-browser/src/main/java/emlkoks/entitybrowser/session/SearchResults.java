package emlkoks.entitybrowser.session;

import emlkoks.entitybrowser.session.entity.ClassDetails;
import emlkoks.entitybrowser.session.entity.EntityWrapper;
import java.util.List;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;


@Getter
public class SearchResults {
    private ObservableList<EntityWrapper> results;
    private ClassDetails classDetails;

    public SearchResults(ClassDetails classDetails, List<Object> resultsList) {
        this.classDetails = classDetails;
        results = resultsList.stream()
                .map(o -> new EntityWrapper(classDetails, o))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
    }
}
