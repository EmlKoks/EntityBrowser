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
import org.hibernate.jpa.criteria.predicate.ComparisonPredicate;
import org.hibernate.jpa.criteria.predicate.LikePredicate;
import org.hibernate.jpa.criteria.predicate.NullnessPredicate;
import org.junit.BeforeClass;
import org.junit.Test;
import test.EntityWithString;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

public class StringComparatorTest {
    private static Comparator comparator;
    private static JpaProvider provider;
    private static Path fieldPath;
    private FieldProperty fieldProperty = mock(FieldProperty.class);

    @BeforeClass
    public static void init() {
        provider = new TestProvider(EntityWithString.class).getProvider();
        fieldPath = UtilClass.buildPath(provider.getCriteriaBuilder(), EntityWithString.class, "testString");
        comparator = new StringComparator();
    }

    @Test
    public void canUseForClass() {
        assertTrue(comparator.canUseForClass(String.class));
    }

    @Test
    public void canNotUseForClass() {
        assertFalse(comparator.canUseForClass(Date.class));
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
    public void createContainsPredicate() {
        var fieldFilter = new FieldFilter(ComparationType.CONTAINS, fieldProperty);
        var predicate = comparator.createPredicate(provider.getCriteriaBuilder(), fieldPath, fieldFilter);
        assertThat(predicate, instanceOf(LikePredicate.class));
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

    @Test(expected = ComparationTypeNotAllowedException.class)
    public void createWrongPredicateForBetween() {
        var fieldFilter = new FieldFilter(ComparationType.BETWEEN, fieldProperty);
        comparator.createPredicate(provider.getCriteriaBuilder(), fieldPath, fieldFilter);
    }

    @Test(expected = ComparationTypeNotAllowedException.class)
    public void createWrongPredicateForGreater() {
        var fieldFilter = new FieldFilter(ComparationType.GREATER, fieldProperty);
        comparator.createPredicate(provider.getCriteriaBuilder(), fieldPath, fieldFilter);
    }

    @Test(expected = ComparationTypeNotAllowedException.class)
    public void createWrongPredicateForLess() {
        var fieldFilter = new FieldFilter(ComparationType.LESS, fieldProperty);
        comparator.createPredicate(provider.getCriteriaBuilder(), fieldPath, fieldFilter);
    }

    @Test(expected = ComparationTypeNotAllowedException.class)
    public void createWrongPredicateForGreaterOrEqual() {
        var fieldFilter = new FieldFilter(ComparationType.GREATER_OR_EQUAL, fieldProperty);
        comparator.createPredicate(provider.getCriteriaBuilder(), fieldPath, fieldFilter);
    }

    @Test(expected = ComparationTypeNotAllowedException.class)
    public void createWrongPredicateForLessOrEqual() {
        var fieldFilter = new FieldFilter(ComparationType.LESS_OR_EQUAL, fieldProperty);
        comparator.createPredicate(provider.getCriteriaBuilder(), fieldPath, fieldFilter);
    }
}