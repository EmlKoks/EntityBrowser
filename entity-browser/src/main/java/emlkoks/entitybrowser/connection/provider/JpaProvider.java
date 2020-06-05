package emlkoks.entitybrowser.connection.provider;

import emlkoks.entitybrowser.connection.Connection;
import emlkoks.entitybrowser.connection.Property;
import emlkoks.entitybrowser.session.entity.EntityList;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.CriteriaBuilder;
import lombok.Getter;

import static emlkoks.entitybrowser.connection.provider.ProviderProperty.*;

public abstract class JpaProvider {
    protected Set<Property> properties = new HashSet<>();
    @Getter
    protected EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;

    public JpaProvider(Connection connection) {
        properties.add(new Property(DRIVER, connection.getDriver().getClassName()));
        properties.add(new Property(URL, connection.getUrl()));
        properties.add(new Property(USER, connection.getUser()));
        properties.add(new Property(PASSWORD, connection.getPassword()));
    }

    public boolean connect(EntityList entityList) {
        entityManagerFactory = createEntityManagerFactory(entityList);
        return true;
    }

    protected abstract EntityManagerFactory createEntityManagerFactory(EntityList entityList);

    public CriteriaBuilder getCriteriaBuilder() {
        return getEntityManager().getCriteriaBuilder();
    }

    public EntityManager getEntityManager() {
        if (Objects.isNull(entityManager)) {
            entityManager = entityManagerFactory.createEntityManager();
        }
        return entityManager;
    }

    protected Map<String, Object> getMapProperties() {
        return properties.stream()
                .collect(
                        Collectors.toMap(
                                Property::getName,
                                Property::getValue
                        ));
    }

}
