package emlkoks.entitybrowser.connection;

import org.junit.Test;

import static org.junit.Assert.*;

public class PropertyTest {

    @Test
    public void setName() {
        var property = new Property();
        property.setName("testName");

        assertEquals("testName", property.getName());
    }

    @Test
    public void setValue() {
        var property = new Property();
        property.setValue(2L);

        assertEquals(2L, property.getValue());
    }

    @Test
    public void allArgsConstructor() {
        var property = new Property("testName", 5L);

        assertEquals("testName", property.getName());
        assertEquals(5L, property.getValue());
    }
}