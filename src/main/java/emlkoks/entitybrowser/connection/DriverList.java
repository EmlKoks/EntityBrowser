package emlkoks.entitybrowser.connection;

import java.util.*;


public class DriverList {
    private Map<String, Driver> drivers = new HashMap<>();

    public  void initalize(){
        drivers.put("MySQL", new Driver("mysql-connector-java-5.1.41-bin.jar", "com.mysql.jdbc.Driver", "url"));
    }

    public DriverList(){
        initalize();
    }

    public Driver getDriver(String name){
        return drivers.get(name);
    }

    public String[] getDriverNames(){
        return Arrays.copyOf(drivers.keySet().toArray(), drivers.size(), String[].class);
    }

}
