package emlkoks.entitybrowser.query.comparator;

import emlkoks.entitybrowser.connection.provider.JpaProvider;
import emlkoks.entitybrowser.connection.provider.TestProvider;
import emlkoks.entitybrowser.query.FieldFilter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.persistence.criteria.Path;
import org.hibernate.jpa.criteria.predicate.ComparisonPredicate;
import org.hibernate.jpa.criteria.predicate.NullnessPredicate;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import test.EntityWithDate;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class DateComparatorTest {
    private Comparator comparator = new DateComparator();
    private JpaProvider provider;
    private Path fieldPath;

    @Before
    public void init() {
        provider = new TestProvider(EntityWithDate.class).getProvider();
        fieldPath = UtilClass.buildPath(provider.getCriteriaBuilder(), EntityWithDate.class, "testDate");
    }

    @Test
    public void canUseForClass() {
        assertTrue(comparator.canUseForClass(Date.class));
    }

    @Test
    public void canNotUseForClass() {
        assertFalse(comparator.canUseForClass(Boolean.class));
        assertFalse(comparator.canUseForClass(String.class));
        assertFalse(comparator.canUseForClass(List.class));
        assertFalse(comparator.canUseForClass(Set.class));
        assertFalse(comparator.canUseForClass(Integer.class));
        assertFalse(comparator.canUseForClass(Float.class));
        assertFalse(comparator.canUseForClass(Long.class));
        assertFalse(comparator.canUseForClass(Double.class));
        assertFalse(comparator.canUseForClass(Short.class));
        assertFalse(comparator.canUseForClass(BigInteger.class));
        assertFalse(comparator.canUseForClass(BigDecimal.class));
    }


    @Test
    public void createNullPredicate() {
        var fieldFilter = new FieldFilter(ComparationType.IS_NULL, null, null);
        var predicate = comparator.createPredicate(provider.getCriteriaBuilder(), fieldPath, fieldFilter);
        assertThat(predicate, instanceOf(NullnessPredicate.class));
    }

    @Test
    public void createNotNullPredicate() {
        var fieldFilter = new FieldFilter(ComparationType.IS_NOT_NULL, null, null);
        var predicate = comparator.createPredicate(provider.getCriteriaBuilder(), fieldPath, fieldFilter);
        assertTrue(predicate.isNegated());
    }

    @Test
    public void createEqualPredicate() {
        var fieldFilter = new FieldFilter(ComparationType.EQUAL, null, null);
        var predicate =
                (ComparisonPredicate) comparator.createPredicate(provider.getCriteriaBuilder(), fieldPath, fieldFilter);
        assertEquals(ComparisonPredicate.ComparisonOperator.EQUAL, predicate.getComparisonOperator());
    }

    @Test
    public void createNotEqualPredicate() {
        var fieldFilter = new FieldFilter(ComparationType.NOT_EQUAL, null, null);
        var predicate =
                (ComparisonPredicate) comparator.createPredicate(provider.getCriteriaBuilder(), fieldPath, fieldFilter);
        assertEquals(ComparisonPredicate.ComparisonOperator.NOT_EQUAL, predicate.getComparisonOperator());
    }

    @Ignore("TODO")
    @Test
    public void createGreaterPredicate() {
        var fieldFilter = new FieldFilter(ComparationType.GREATER, null, null);
        var predicate =
                (ComparisonPredicate) comparator.createPredicate(provider.getCriteriaBuilder(), fieldPath, fieldFilter);
        assertEquals(ComparisonPredicate.ComparisonOperator.GREATER_THAN, predicate.getComparisonOperator());
    }

    @Ignore("TODO")
    @Test
    public void createGreaterOrEqualPredicate() {
        var fieldFilter = new FieldFilter(ComparationType.GREATER, null, null);
        var predicate =
                (ComparisonPredicate) comparator.createPredicate(provider.getCriteriaBuilder(), fieldPath, fieldFilter);
        assertEquals(ComparisonPredicate.ComparisonOperator.GREATER_THAN_OR_EQUAL, predicate.getComparisonOperator());
    }

    @Ignore("TODO")
    @Test
    public void createLessPredicate() {
        var fieldFilter = new FieldFilter(ComparationType.LESS, null, null);
        var predicate =
                (ComparisonPredicate) comparator.createPredicate(provider.getCriteriaBuilder(), fieldPath, fieldFilter);
        assertEquals(ComparisonPredicate.ComparisonOperator.LESS_THAN, predicate.getComparisonOperator());
    }

    @Ignore("TODO")
    @Test
    public void createLessOrEqualPredicate() {
        var fieldFilter = new FieldFilter(ComparationType.LESS_OR_EQUAL, null, null);
        var predicate =
                (ComparisonPredicate) comparator.createPredicate(provider.getCriteriaBuilder(), fieldPath, fieldFilter);
        assertEquals(ComparisonPredicate.ComparisonOperator.LESS_THAN_OR_EQUAL, predicate.getComparisonOperator());
    }

    @Ignore("TODO")
    @Test
    public void createBetweenPredicate() {
        var fieldFilter = new FieldFilter(ComparationType.BETWEEN, null, null);
        var predicate =
                (ComparisonPredicate) comparator.createPredicate(provider.getCriteriaBuilder(), fieldPath, fieldFilter);
//        assertEquals(ComparisonPredicate.ComparisonOperator., predicate.getComparisonOperator());TODO
    }

    @Test(expected = ComparationTypeNotAllowedException.class)
    public void createWrongPredicateForContains() {
        var fieldFilter = new FieldFilter(ComparationType.CONTAINS, null, null);
        comparator.createPredicate(provider.getCriteriaBuilder(), fieldPath, fieldFilter);
    }
}