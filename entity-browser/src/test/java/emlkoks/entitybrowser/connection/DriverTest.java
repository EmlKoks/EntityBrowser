package emlkoks.entitybrowser.connection;

import emlkoks.entitybrowser.common.Resources;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import static org.junit.Assert.*;

public class DriverTest {

    @Test
    public void setName() {
        var driver = new Driver();
        driver.setName("testName");
        assertEquals("testName", driver.getName());
    }

    @Test
    public void equalsName() {
        var driver = new Driver();
        driver.setName("testName");
        assertTrue(driver.equalsName("testName"));
    }

    @Test
    public void notEqualsName() {
        var driver = new Driver();
        driver.setName("testName");
        assertFalse(driver.equalsName("otherName"));
        assertFalse(driver.equalsName(null));
    }

    @Test
    public void notEqualsNullName() {
        var driver = new Driver();
        assertFalse(driver.equalsName("otherName"));
        assertFalse(driver.equalsName(null));
    }

    @Test
    public void setLib() {
        var driver = new Driver();
        driver.setLibraryPath("testLibraryPath");
        assertEquals("testLibraryPath", driver.getLibraryPath());
    }

    @Test
    public void setClassName() {
        var driver = new Driver();
        driver.setClassName("testClassName");
        assertEquals("testClassName", driver.getClassName());
    }

    @Test
    public void setUrlTemplate() {
        var driver = new Driver();
        driver.setUrlTemplate("testUrlTemplate");
        assertEquals("testUrlTemplate", driver.getUrlTemplate());
    }

    @Test(expected = DriverNotFoundException.class)
    public void failedLoadDriver() {
        var driver = new Driver();
        driver.setLibraryPath("testPath");
        driver.loadDriver();
    }

    @Test
    public void correctLoadDriver() throws NoSuchFieldException, IllegalAccessException {
        setDriverDirPathToTestResources();
        var driver = new Driver();
        driver.setLibraryPath("testDriver.jar");
        driver.loadDriver();
    }

    @Test
    public void driverWasLoaded() throws NoSuchFieldException, IllegalAccessException {
        setDriverDirPathToTestResources();
        var driver = new Driver();
        driver.setLibraryPath("testDriver.jar");
        driver.loadDriver();
        driver.loadDriver();//TODO check private field wasLoaded or count number call to add lib to classLoder
    }

    private void setDriverDirPathToTestResources() throws NoSuchFieldException, IllegalAccessException {
        var field = Resources.class.getDeclaredField("DRIVERS_DIR_PATH");
        field.setAccessible(true);
        var modifiers = Field.class.getDeclaredField("modifiers");
        modifiers.setAccessible(true);
        modifiers.setInt(field, field.getModifiers() & ~Modifier.FINAL);
        field.set(null, getClass().getClassLoader().getResource("").getPath());
    }
}