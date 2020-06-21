package emlkoks.entitybrowser.query;

import emlkoks.entitybrowser.query.comparator.ComparationType;
import emlkoks.entitybrowser.session.entity.FieldProperty;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

public class FieldFilterTest {
    private FieldProperty fieldProperty = mock(FieldProperty.class);

    @Test(expected = NullPointerException.class)
    public void createFieldFirlterWithNullComparationType() {
        new FieldFilter(null, null);
    }

    @Test(expected = NullPointerException.class)
    public void createFieldFirlterWithFieldProperty() {
        new FieldFilter(ComparationType.IS_NULL, null);
    }

    @Test
    public void createFieldFirlterWithoutValues() {
        var fieldFilter = new FieldFilter(ComparationType.IS_NULL, fieldProperty);
        assertEquals(fieldProperty, fieldFilter.getFieldProperty());
        assertEquals(ComparationType.IS_NULL, fieldFilter.getComparationType());
        assertNull(fieldFilter.getValue());
        assertNull(fieldFilter.getValue(0));
        assertNull(fieldFilter.getValue(1));
    }

    @Test
    public void createFieldFirlterWithValue() {
        var fieldFilter = new FieldFilter(ComparationType.IS_NULL, fieldProperty, 2);
        assertEquals(ComparationType.IS_NULL, fieldFilter.getComparationType());
        assertEquals(2, fieldFilter.getValue());
        assertEquals(2, fieldFilter.getValue(0));
        assertNull(fieldFilter.getValue(1));
        assertNull(fieldFilter.getValue(2));
    }

    @Test
    public void createFieldFirlterWithValues() {
        var fieldFilter = new FieldFilter(ComparationType.IS_NULL, fieldProperty, 2, 3, 4);
        assertEquals(ComparationType.IS_NULL, fieldFilter.getComparationType());
        assertEquals(3, fieldFilter.getValues().length);
        assertEquals(2, fieldFilter.getValue(0));
        assertEquals(3, fieldFilter.getValue(1));
        assertEquals(4, fieldFilter.getValue(2));
        assertNull(fieldFilter.getValue(3));
        assertNull(fieldFilter.getValue(4));
        assertNull(fieldFilter.getValue(5));
    }

}