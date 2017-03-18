package emlkoks.entitybrowser.drivers;

import java.util.HashMap;
import java.util.Map;


public class DriverList {
    private static Map<String, Driver> drivers = new HashMap<>();

    static{
        initalize();
    }

    public static void initalize(){
        drivers.put("MySQL", new Driver("mysql-connector-java-5.1.41-bin.jar", "com.mysql.jdbc.Driver", "url"));
    }

    public static Driver getDriver(String name){
        return drivers.get(name);
    }

}
