package emlkoks.entitybrowser.connection;

import emlkoks.entitybrowser.Main;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import lombok.Data;

/**
 * Created by EmlKoks on 18.03.17.
 */
@Data
public class Connection {
    private String name;
    private Driver driver;
    private String url;
    private String user;
    private String password;

    @XmlTransient
    public Driver getDriver() {
        return driver;
    }

    @XmlElement(name = "driver")
    public String getDriverS() {
        return driver.getName();
    }

    public void setDriverS(String driverName) {
        driver = Main.drivers.getDriver(driverName);
    }
}
