package emlkoks.entitybrowser.connection;

import emlkoks.entitybrowser.resources.Resources;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlTransient;
import java.io.File;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

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
    private boolean wasLoaded=false;

    public Driver(String lib, String className, String url) {
        this.lib = lib;
        this.className = className;
        this.url = url;
    }

    public URL getLibURL() {
        try {
            return new File(Resources.DRIVERS_DIR +lib).toURL();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void loadDriver(){
        if(wasLoaded) return;
        Method method;
        try {
            method = URLClassLoader.class.getDeclaredMethod("addURL", new Class[]{URL.class});
            method.setAccessible(true);
            method.invoke(ClassLoader.getSystemClassLoader(), new Object[]{getLibURL()});
            wasLoaded=true;
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
        }
    }
}
