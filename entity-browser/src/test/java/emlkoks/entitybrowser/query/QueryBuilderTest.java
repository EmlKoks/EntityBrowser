package emlkoks.entitybrowser.query;

import emlkoks.entitybrowser.connection.provider.JpaProvider;
import emlkoks.entitybrowser.connection.provider.TestProvider;
import emlkoks.entitybrowser.query.comparator.ComparationType;
import emlkoks.entitybrowser.session.entity.ClassDetails;
import emlkoks.entitybrowser.session.entity.FieldProperty;
import java.util.Arrays;
import org.hibernate.jpa.criteria.predicate.ComparisonPredicate;
import org.junit.Before;
import org.junit.Test;
import test.EntityWithInteger;

import static org.junit.Assert.assertEquals;

public class QueryBuilderTest {
    private JpaProvider provider;
    private ClassDetails classDetails;
    private FieldProperty idProperty;

    @Before
    public void init() {
        provider = new TestProvider(EntityWithInteger.class).getProvider();
        classDetails = new ClassDetails(EntityWithInteger.class);
        idProperty = classDetails.getFieldProperty("id");
    }

    @Test
    public void getCriteriaQueryWithoutFilters() {
        ClassDetails classDetails = new ClassDetails(EntityWithInteger.class);
        QueryBuilder queryBuilder = new QueryBuilder(provider.getCriteriaBuilder(), classDetails, null);
        var criteriaQuery = queryBuilder.buildCriteriaQuery();
        assertEquals(EntityWithInteger.class, criteriaQuery.getResultType());
    }

    @Test
    public void getCriteriaQueryWithOneIdFilter() {
        FieldFilter fieldFilter = new FieldFilter(ComparationType.EQUAL, idProperty, 1);
        QueryBuilder queryBuilder =
                new QueryBuilder(provider.getCriteriaBuilder(), classDetails, Arrays.asList(fieldFilter));
        var criteriaQuery = queryBuilder.buildCriteriaQuery();
        assertEquals(EntityWithInteger.class, criteriaQuery.getResultType());
        assertEquals(1, criteriaQuery.getRestriction().getExpressions().size());
        var predicate = (ComparisonPredicate) criteriaQuery.getRestriction().getExpressions().get(0);
        assertEquals(ComparisonPredicate.ComparisonOperator.EQUAL, predicate.getComparisonOperator());
    }

    @Test
    public void getCriteriaQueryWithTwoIdFilter() {
        FieldFilter fieldFilter = new FieldFilter(ComparationType.EQUAL, idProperty, 1);
        FieldFilter fieldFilter2 = new FieldFilter(ComparationType.NOT_EQUAL, idProperty, 2);
        QueryBuilder queryBuilder = new QueryBuilder(provider.getCriteriaBuilder(), classDetails,
                Arrays.asList(fieldFilter, fieldFilter2));
        var criteriaQuery = queryBuilder.buildCriteriaQuery();
        assertEquals(EntityWithInteger.class, criteriaQuery.getResultType());
        assertEquals(2, criteriaQuery.getRestriction().getExpressions().size());
        var predicate1 = (ComparisonPredicate) criteriaQuery.getRestriction().getExpressions().get(0);
        assertEquals(ComparisonPredicate.ComparisonOperator.EQUAL, predicate1.getComparisonOperator());
        var predicate2 = (ComparisonPredicate) criteriaQuery.getRestriction().getExpressions().get(1);
        assertEquals(ComparisonPredicate.ComparisonOperator.NOT_EQUAL, predicate2.getComparisonOperator());
    }


}