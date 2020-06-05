package emlkoks.entitybrowser.session;

import emlkoks.entitybrowser.connection.Connection;
import emlkoks.entitybrowser.connection.provider.JpaProvider;
import emlkoks.entitybrowser.connection.provider.ProviderFactory;
import emlkoks.entitybrowser.session.entity.ClassDetails;
import emlkoks.entitybrowser.session.entity.EntityList;
import emlkoks.entitybrowser.session.exception.LibraryFileNotFoundException;
import java.io.File;
import java.util.List;
import lombok.Getter;

/**
 * Created by EmlKoks on 09.04.17.
 */
public class Session {
    protected EntityList entityList;
    private Connection connection;
    @Getter
    private JpaProvider provider;

    public Session(Connection connection) {
        this.connection = connection;
        try {
            entityList = new EntityList(new File(connection.getLibraryPath()));
        } catch (LibraryFileNotFoundException exception) {
            exception.printStackTrace();
            //TODO
        }
    }

    public List<String> getClassNames() {
        return entityList.getClassNames();
    }

    public boolean connect() {
        provider = new ProviderFactory().getProvider(connection);
        return provider.connect(entityList);

    }

    public ClassDetails getEntity(String entityName) {
        return entityList.getEntity(entityName);
    }
}
