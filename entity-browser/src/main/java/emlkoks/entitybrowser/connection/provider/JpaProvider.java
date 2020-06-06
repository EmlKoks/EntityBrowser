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
        if (Objects.isNull(connection.getDriver())
                || Objects.isNull(connection.getDriver().getClassName())) {
            throw new RuntimeException("Driver class cannot be null");
        }
        properties.add(new Property(DRIVER, connection.getDriver().getClassName()));

        if (Objects.isNull(connection.getUrl())) {
            throw new RuntimeException("Url cannot be null");
        }
        properties.add(new Property(URL, connection.getUrl()));
        properties.add(new Property(USER, connection.getUser()));
        properties.add(new Property(PASSWORD, connection.getPassword()));
    }

    public boolean connect(EntityList entityList) {
        if (Objects.isNull(entityList)) {
            throw new NullPointerException("EntityList cannot be null");
        }
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
                .filter(property -> Objects.nonNull(property.getValue()))
                .collect(
                        Collectors.toMap(
                                Property::getName,
                                Property::getValue
                        ));
    }

}
