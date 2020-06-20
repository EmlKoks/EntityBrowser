package emlkoks.entitybrowser.query;

import emlkoks.entitybrowser.connection.ConnectionTest;
import emlkoks.entitybrowser.connection.provider.HibernateProvider;
import emlkoks.entitybrowser.connection.provider.JpaProvider;
import emlkoks.entitybrowser.query.comparator.ComparationType;
import emlkoks.entitybrowser.session.entity.ClassDetails;
import emlkoks.entitybrowser.session.entity.EntityList;
import emlkoks.entitybrowser.session.entity.FieldProperty;
import java.util.Arrays;
import javax.persistence.Entity;
import javax.persistence.Id;
import org.hibernate.jpa.criteria.predicate.ComparisonPredicate;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class QueryBuilderTest {
    private JpaProvider provider;
    private EntityList entityList = mock(EntityList.class);

    @Before
    public void init() {
        provider = new HibernateProvider(ConnectionTest.createH2Connection());
    }

    private void connectproviderWithEntity(Class entity) {
        when(entityList.getClasses()).thenReturn(Arrays.asList(entity));
        when(entityList.hasClasses()).thenReturn(true);
        provider.connect(entityList);
    }

    @Test
    public void getCriteriaQueryWithoutFilters() {
        @Entity
        class Test {
            @Id
            private Long id;
        }

        connectproviderWithEntity(Test.class);
        ClassDetails classDetails = new ClassDetails(Test.class);
        QueryBuilder queryBuilder = new QueryBuilder(provider.getCriteriaBuilder(), classDetails, null);
        var criteriaQuery = queryBuilder.buildCriteriaQuery();
        assertEquals(Test.class, criteriaQuery.getResultType());
    }

    @Test
    public void getCriteriaQueryWithOneIdFilter() {
        @Entity
        class Test {
            @Id
            private Long id;
        }

        connectproviderWithEntity(Test.class);
        ClassDetails classDetails = new ClassDetails(Test.class);
        FieldProperty idProperty = classDetails.getFieldProperty("id");
        FieldFilter fieldFilter = new FieldFilter(ComparationType.EQUAL, idProperty, 1);
        QueryBuilder queryBuilder =
                new QueryBuilder(provider.getCriteriaBuilder(), classDetails, Arrays.asList(fieldFilter));
        var criteriaQuery = queryBuilder.buildCriteriaQuery();
        assertEquals(Test.class, criteriaQuery.getResultType());
        assertEquals(1, criteriaQuery.getRestriction().getExpressions().size());
        var predicate = (ComparisonPredicate) criteriaQuery.getRestriction().getExpressions().get(0);
        assertEquals(ComparisonPredicate.ComparisonOperator.EQUAL, predicate.getComparisonOperator());
    }

    @Test
    public void getCriteriaQueryWithTwoIdFilter() {
        @Entity
        class Test {
            @Id
            private Long id;
        }

        connectproviderWithEntity(Test.class);
        ClassDetails classDetails = new ClassDetails(Test.class);
        FieldProperty idProperty = classDetails.getFieldProperty("id");
        FieldFilter fieldFilter = new FieldFilter(ComparationType.EQUAL, idProperty, 1);
        FieldFilter fieldFilter2 = new FieldFilter(ComparationType.NOT_EQUAL, idProperty, 2);
        QueryBuilder queryBuilder = new QueryBuilder(provider.getCriteriaBuilder(), classDetails,
                Arrays.asList(fieldFilter, fieldFilter2));
        var criteriaQuery = queryBuilder.buildCriteriaQuery();
        assertEquals(Test.class, criteriaQuery.getResultType());
        assertEquals(2, criteriaQuery.getRestriction().getExpressions().size());
        var predicate1 = (ComparisonPredicate) criteriaQuery.getRestriction().getExpressions().get(0);
        assertEquals(ComparisonPredicate.ComparisonOperator.EQUAL, predicate1.getComparisonOperator());
        var predicate2 = (ComparisonPredicate) criteriaQuery.getRestriction().getExpressions().get(1);
        assertEquals(ComparisonPredicate.ComparisonOperator.NOT_EQUAL, predicate2.getComparisonOperator());
    }


}