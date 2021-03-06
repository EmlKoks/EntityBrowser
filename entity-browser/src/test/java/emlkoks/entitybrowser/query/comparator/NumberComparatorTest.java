package emlkoks.entitybrowser.query.comparator;

import emlkoks.entitybrowser.connection.provider.JpaProvider;
import emlkoks.entitybrowser.connection.provider.TestProvider;
import emlkoks.entitybrowser.query.FieldFilter;
import emlkoks.entitybrowser.session.entity.FieldProperty;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.persistence.criteria.Path;
import org.hibernate.jpa.criteria.predicate.BetweenPredicate;
import org.hibernate.jpa.criteria.predicate.ComparisonPredicate;
import org.hibernate.jpa.criteria.predicate.NullnessPredicate;
import org.junit.BeforeClass;
import org.junit.Test;
import test.EntityWithInteger;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

public class NumberComparatorTest {
    private static Comparator comparator;
    private static JpaProvider provider;
    private static Path fieldPath;
    private FieldProperty fieldProperty = mock(FieldProperty.class);

    @BeforeClass
    public static void init() {
        provider = new TestProvider(EntityWithInteger.class).getProvider();
        fieldPath = UtilClass.buildPath(provider.getCriteriaBuilder(), EntityWithInteger.class, "testInteger");
        comparator = new NumberComparator();
    }

    @Test
    public void canUseForClass() {
        assertTrue(comparator.canUseForClass(int.class));
        assertTrue(comparator.canUseForClass(float.class));
        assertTrue(comparator.canUseForClass(long.class));
        assertTrue(comparator.canUseForClass(double.class));
        assertTrue(comparator.canUseForClass(short.class));
        assertTrue(comparator.canUseForClass(Integer.class));
        assertTrue(comparator.canUseForClass(Float.class));
        assertTrue(comparator.canUseForClass(Long.class));
        assertTrue(comparator.canUseForClass(Double.class));
        assertTrue(comparator.canUseForClass(Short.class));
        assertTrue(comparator.canUseForClass(BigInteger.class));
        assertTrue(comparator.canUseForClass(BigDecimal.class));
    }

    @Test
    public void canNotUseForClass() {
        assertFalse(comparator.canUseForClass(Date.class));
        assertFalse(comparator.canUseForClass(Boolean.class));
        assertFalse(comparator.canUseForClass(String.class));
        assertFalse(comparator.canUseForClass(List.class));
        assertFalse(comparator.canUseForClass(Set.class));
    }

    @Test
    public void createNullPredicate() {
        var fieldFilter = new FieldFilter(ComparationType.IS_NULL, fieldProperty);
        var predicate = comparator.createPredicate(provider.getCriteriaBuilder(), fieldPath, fieldFilter);
        assertThat(predicate, instanceOf(NullnessPredicate.class));
    }

    @Test
    public void createNotNullPredicate() {
        var fieldFilter = new FieldFilter(ComparationType.IS_NOT_NULL, fieldProperty);
        var predicate = comparator.createPredicate(provider.getCriteriaBuilder(), fieldPath, fieldFilter);
        assertTrue(predicate.isNegated());
    }

    @Test
    public void createEqualPredicate() {
        var fieldFilter = new FieldFilter(ComparationType.EQUAL, fieldProperty);
        var predicate =
                (ComparisonPredicate) comparator.createPredicate(provider.getCriteriaBuilder(), fieldPath, fieldFilter);
        assertEquals(ComparisonPredicate.ComparisonOperator.EQUAL, predicate.getComparisonOperator());
    }

    @Test
    public void createNotEqualPredicate() {
        var fieldFilter = new FieldFilter(ComparationType.NOT_EQUAL, fieldProperty);
        var predicate =
                (ComparisonPredicate) comparator.createPredicate(provider.getCriteriaBuilder(), fieldPath, fieldFilter);
        assertEquals(ComparisonPredicate.ComparisonOperator.NOT_EQUAL, predicate.getComparisonOperator());
    }

    @Test
    public void createGreaterPredicate() {
        var fieldFilter = new FieldFilter(ComparationType.GREATER, fieldProperty);
        var predicate =
                (ComparisonPredicate) comparator.createPredicate(provider.getCriteriaBuilder(), fieldPath, fieldFilter);
        assertEquals(ComparisonPredicate.ComparisonOperator.GREATER_THAN, predicate.getComparisonOperator());
    }

    @Test
    public void createGreaterOrEqualPredicate() {
        var fieldFilter = new FieldFilter(ComparationType.GREATER_OR_EQUAL, fieldProperty);
        var predicate =
                (ComparisonPredicate) comparator.createPredicate(provider.getCriteriaBuilder(), fieldPath, fieldFilter);
        assertEquals(ComparisonPredicate.ComparisonOperator.GREATER_THAN_OR_EQUAL, predicate.getComparisonOperator());
    }

    @Test
    public void createLessPredicate() {
        var fieldFilter = new FieldFilter(ComparationType.LESS, fieldProperty);
        var predicate =
                (ComparisonPredicate) comparator.createPredicate(provider.getCriteriaBuilder(), fieldPath, fieldFilter);
        assertEquals(ComparisonPredicate.ComparisonOperator.LESS_THAN, predicate.getComparisonOperator());
    }

    @Test
    public void createLessOrEqualPredicate() {
        var fieldFilter = new FieldFilter(ComparationType.LESS_OR_EQUAL, fieldProperty);
        var predicate =
                (ComparisonPredicate) comparator.createPredicate(provider.getCriteriaBuilder(), fieldPath, fieldFilter);
        assertEquals(ComparisonPredicate.ComparisonOperator.LESS_THAN_OR_EQUAL, predicate.getComparisonOperator());
    }

    @Test
    public void createBetweenPredicate() {
        var fieldFilter = new FieldFilter(ComparationType.BETWEEN, fieldProperty, 1, 2);
        var predicate = comparator.createPredicate(provider.getCriteriaBuilder(), fieldPath, fieldFilter);
        assertThat(predicate, instanceOf(BetweenPredicate.class));
    }

    @Test(expected = ComparationTypeNotAllowedException.class)
    public void createWrongPredicateForContains() {
        var fieldFilter = new FieldFilter(ComparationType.CONTAINS, fieldProperty);
        comparator.createPredicate(provider.getCriteriaBuilder(), fieldPath, fieldFilter);
    }

}