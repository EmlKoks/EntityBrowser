package emlkoks.entitybrowser.connection;

import emlkoks.entitybrowser.Util;

import javax.xml.bind.annotation.XmlTransient;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * Created by koks on 10.03.17.
 */
public class Driver {
    private String name;
    private String lib;
    private String className;
    private String url;
    @XmlTransient
    private boolean wasLoaded=false;

    public Driver(){}

    public Driver(String lib, String className, String url) {
        this.lib = lib;
        this.className = className;
        this.url = url;
    }

    public URL getLibURL() {
        try {
            return new File(Util.driversDir+lib).toURL();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getLib(){
        return lib;
    }

    public void setLib(String lib) {
        this.lib = lib;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
