package emlkoks.entitybrowser.connection;

import emlkoks.entitybrowser.Main;
import emlkoks.entitybrowser.Util;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class DriverList {
    private List<Driver> drivers = new ArrayList<>();

    public  void initalize(){
//        Driver d = new Driver("mysql-connector-java-5.1.41-bin.jar", "com.mysql.jdbc.Driver", "url");
//        d.setName("MySQL");
//        drivers.add(d);
    }

    public DriverList(){
        initalize();
    }

    public Driver getDriver(String name){
        if(Util.isNullOrEmpty(name)) return null;
        for(Driver d : drivers)
            if(name.equals(d.getName()))
                return d;
        return null;
    }

    public List<String> getDriverNames(){
        List<String> names = new ArrayList<>();
        drivers.forEach(x -> names.add(x.getName()));
        return names;
    }

    public void add(Driver sc){
        drivers.add(sc);
        Main.marshal(this, Util.drivers);
    }


    public List<Driver> getDrivers() {
        return drivers;
    }

    public void setDrivers(List<Driver> drivers) {
        this.drivers = drivers;
    }

}
