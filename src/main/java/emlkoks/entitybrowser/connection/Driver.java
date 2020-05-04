package emlkoks.entitybrowser.connection;

import emlkoks.entitybrowser.common.Resources;
import java.io.File;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import javax.xml.bind.annotation.XmlTransient;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by koks on 10.03.17.
 */
@Data
@NoArgsConstructor
public class Driver {
    private String name;
    private String lib;
    private String className;
    private String url;
    @XmlTransient
    private boolean wasLoaded = false;

    private URL getLibUrl() {
        try {
            return new File(Resources.DRIVERS_DIR + lib).toURL();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void loadDriver() {
        if (wasLoaded) {
            return;
        }
        try {
            Method method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
            method.setAccessible(true);
            method.invoke(ClassLoader.getSystemClassLoader(), getLibUrl());
            wasLoaded = true;
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
        }
    }
}
