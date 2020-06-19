package emlkoks.entitybrowser.query.comparator;

import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

public class NumberComparatorTest {

    @Test
    public void canUseForClass() {
        var numberComparator = new NumberComparator();
        assertTrue(numberComparator.canUseForClass(int.class));
        assertTrue(numberComparator.canUseForClass(float.class));
        assertTrue(numberComparator.canUseForClass(long.class));
        assertTrue(numberComparator.canUseForClass(double.class));
        assertTrue(numberComparator.canUseForClass(short.class));
        assertTrue(numberComparator.canUseForClass(Integer.class));
        assertTrue(numberComparator.canUseForClass(Float.class));
        assertTrue(numberComparator.canUseForClass(Long.class));
        assertTrue(numberComparator.canUseForClass(Double.class));
        assertTrue(numberComparator.canUseForClass(Short.class));
        assertTrue(numberComparator.canUseForClass(BigInteger.class));
        assertTrue(numberComparator.canUseForClass(BigDecimal.class));
    }

    @Test
    public void canNotUseForClass() {
        var numberComparator = new NumberComparator();
        assertFalse(numberComparator.canUseForClass(Date.class));
        assertFalse(numberComparator.canUseForClass(String.class));
        assertFalse(numberComparator.canUseForClass(List.class));
        assertFalse(numberComparator.canUseForClass(Set.class));
    }

//    @Test
//    public void createPredicate() {
//        var numberComparator = new NumberComparator();
//        numberComparator.createPredicate()
//    }

}