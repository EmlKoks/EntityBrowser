package emlkoks.entitybrowser.common;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class CustomClassLoader extends URLClassLoader {
    public CustomClassLoader(ClassLoader parent) {
        super(new URL[0], parent);
    }

    public void addLib(File file) {
        try {
            addURL(file.toURI().toURL());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
}
