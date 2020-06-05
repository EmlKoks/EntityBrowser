package emlkoks.entitybrowser.connection;

import emlkoks.entitybrowser.Main;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import lombok.Data;

/**
 * Created by EmlKoks on 18.03.17.
 */
@Data
public class Connection implements Cloneable {
    @NotNull
    private Integer id;
    @NotBlank
    private String name;
    private Driver driver;
    private String url;
    private String user;
    private String password;
    private String libraryPath;
    private Provider provider;
    private ObservableList<Property> properties = FXCollections.observableArrayList();

    @XmlTransient
    public Driver getDriver() {
        return driver;
    }

    @XmlElement(name = "driver")
    public String getDriverS() {
        if (Objects.nonNull(driver)) {
            return driver.getName();
        }
        return null;
    }

    public void setDriverS(String driverName) {
        driver = Main.drivers.getDriver(driverName);
    }

    @Override
    public Connection clone() {
        try {
            return (Connection)super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean connectionTest() {
        driver.loadDriver();
        try {
            DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
