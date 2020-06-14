package emlkoks.entitybrowser.connection;

import com.google.common.base.Strings;
import emlkoks.entitybrowser.Main;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

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
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private File libraryFile;
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

    public File getLibrary() {
        return libraryFile;
    }

    @XmlElement(name = "libraryFile")
    public String getLibraryPath() {
        if (Objects.isNull(libraryFile)) {
            return null;
        }
        return libraryFile.getAbsolutePath();
    }

    public boolean setLibraryPath(String libraryPath) {
        if (Strings.isNullOrEmpty(libraryPath)) {
            libraryFile = null;
            return false;
        }
        File library = new File(libraryPath);
        if (!library.exists()) {
            libraryFile = null;
            return false;
        }
        libraryFile = library;
        return true;
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
