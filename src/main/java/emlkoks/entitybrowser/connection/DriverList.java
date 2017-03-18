package emlkoks.entitybrowser.connection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DriverList {
    private Map<String, Driver> drivers = new HashMap<>();
    public static List<String> savedConnection = new ArrayList<>();

    public  void initalize(){
        drivers.put("MySQL", new Driver("mysql-connector-java-5.1.41-bin.jar", "com.mysql.jdbc.Driver", "url"));
    }

    public DriverList(){
        initalize();
        savedConnection.add("Test1");
        savedConnection.add("Test2");

    }

    public Driver getDriver(String name){
        return drivers.get(name);
    }

}
