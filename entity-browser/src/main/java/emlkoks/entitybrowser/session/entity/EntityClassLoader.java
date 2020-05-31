package emlkoks.entitybrowser.session.entity;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class EntityClassLoader extends URLClassLoader {
    public EntityClassLoader(ClassLoader parent) {
        super(new URL[0], parent);
    }

    public void addLib(File file) {
        try {
            addURL(file.toURL());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
}
