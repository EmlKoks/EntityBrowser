package emlkoks.entitybrowser.connection.provider;

import emlkoks.entitybrowser.connection.ConnectionTest;
import emlkoks.entitybrowser.session.entity.EntityList;
import java.util.Arrays;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import lombok.Getter;
import test.IdEntity;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestProvider {
    @Getter
    private JpaProvider provider;
    private Class<? extends IdEntity> entityClass;

    public TestProvider(Class<? extends IdEntity> testedClass) {
        entityClass = testedClass;
        provider = new HibernateProvider(ConnectionTest.createH2Connection());
        var entityList = mock(EntityList.class);
        when(entityList.getClasses()).thenReturn(Arrays.asList(testedClass));
        when(entityList.hasClasses()).thenReturn(true);
        provider.connect(entityList);
    }

    public void addEntities(IdEntity... entities) {
        var em = provider.getEntityManager();
        em.getTransaction().begin();
        IntStream.range(0, entities.length)
                .mapToObj(i -> {
                    entities[i].setId((long)i);
                    return entities[i];
                })
                .forEach(em::persist);
    }

    public void addEntities(int numberOfEntities) {
        var em = provider.getEntityManager();
        em.getTransaction().begin();
        LongStream.range(0, numberOfEntities)
                .mapToObj(i -> {
                    IdEntity entity = null;
                    try {
                        entity = entityClass.newInstance();
                    } catch (InstantiationException | IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    entity.setId(i);
                    return entity;
                })
                .forEach(em::persist);
    }
}
