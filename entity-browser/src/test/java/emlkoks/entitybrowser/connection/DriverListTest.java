package emlkoks.entitybrowser.connection;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class DriverListTest {

    @Test
    public void getDriver() {
        var driverList = new DriverList();
        var driver = new Driver();
        driver.setName("testDriver");
        driverList.add(driver);
        assertEquals(driver, driverList.getDriver("testDriver"));
    }

    @Test
    public void getNullDriver() {
        var driverList = new DriverList();
        assertNull(driverList.getDriver("testDriver"));
    }

    @Test
    public void getEmptyDriver() {
        var driverList = new DriverList();
        assertNull(driverList.getDriver(""));
    }

    @Test
    public void getWringDriver() {
        var driverList = new DriverList();
        assertNull(driverList.getDriver("test"));
    }

    @Test
    public void getDriverNames() {
        var driverList = new DriverList();
        var driver = new Driver();
        driver.setName("testDriver");
        driverList.add(driver);

        var driver2 = new Driver();
        driver2.setName("testDriver2");
        driverList.add(driver2);

        assertEquals(Arrays.asList("testDriver", "testDriver2"), driverList.getDriverNames());
    }
    @Test
    public void getWrongDriverNames() {
        var driverList = new DriverList();
        var driver = new Driver();
        driver.setName("testDriver");
        driverList.add(driver);

        var driver2 = new Driver();
        driver2.setName("testDriver2");
        driverList.add(driver2);

        assertNotEquals(Arrays.asList("testDriverNotInList", "testDriverNotInList2"), driverList.getDriverNames());
    }

    @Test
    public void add() {
    }
}