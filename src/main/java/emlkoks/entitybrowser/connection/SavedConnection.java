package emlkoks.entitybrowser.connection;

import javax.xml.bind.annotation.*;

/**
 * Created by EmlKoks on 18.03.17.
 */
public class SavedConnection {
    private String name;
    private Driver driver;
    private String url;
    private String user;
    private String password;

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
