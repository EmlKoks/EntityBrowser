package emlkoks.entitybrowser.session;

import emlkoks.entitybrowser.connection.Connection;
import emlkoks.entitybrowser.connection.provider.JpaProvider;
import emlkoks.entitybrowser.connection.provider.ProviderFactory;
import emlkoks.entitybrowser.session.entity.ClassDetails;
import emlkoks.entitybrowser.session.entity.EntityList;
import emlkoks.entitybrowser.session.exception.LibraryFileNotFoundException;

import java.util.Collection;
import java.util.Objects;

import lombok.Getter;

/**
 * Created by EmlKoks on 09.04.17.
 */
public class Session {
    protected EntityList entityList;
    private Connection connection;
    @Getter
    private JpaProvider provider;

    public Session(Connection connection) throws LibraryFileNotFoundException {
        if (Objects.isNull(connection)) {
            throw new NullPointerException("Connection cannot be null");
        }
        this.connection = connection;
        entityList = new EntityList(connection.getLibrary());
    }

    public Collection<ClassDetails> getClasses() {
        return entityList.getClassesDetails();
    }

    public boolean connect() {
        provider = ProviderFactory.getProvider(connection);
        return provider.connect(entityList);

    }

    public ClassDetails getEntity(String entityName) {
        return entityList.getEntity(entityName);
    }
}
