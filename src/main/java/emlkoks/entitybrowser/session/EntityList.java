package emlkoks.entitybrowser.session;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by EmlKoks on 03.04.17.
 */
public class EntityList {
    Map<String, Entity> classList = new HashMap<>();

    public void loadEntities(File file) {
        loadLib(file);
        try {
            ZipInputStream zip = new ZipInputStream(new FileInputStream(file));
            for (ZipEntry entry = zip.getNextEntry(); entry != null; entry = zip.getNextEntry()) {
                if (!entry.isDirectory() && entry.getName().endsWith(".class") && !entry.getName().endsWith("_.class")) {
                    String className = entry.getName().replace('/', '.').replace(".class", "");
                    try {
                        classList.put(className.substring(className.lastIndexOf(".")+1),
                                new Entity(Class.forName(className)));
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Class> getClasses(){
        List<Class> list = new ArrayList<>();
        classList.values().forEach(x -> list.add(x.getClazz()));
        return list;
    }

    public List<String> getClassNames(){
        ArrayList<String> names = new ArrayList<>();
        classList.keySet().forEach(x -> names.add(x));
        return names;
    }

    public Entity getEntity(String entityName){
        return classList.get(entityName);
    }

    private void loadLib(File lib){
        Method method;
        try {
            method = URLClassLoader.class.getDeclaredMethod("addURL", new Class[]{URL.class});
            method.setAccessible(true);
            System.out.println("lib.toURL().toString() = " + lib.toURL().toString());
            method.invoke(ClassLoader.getSystemClassLoader(), new Object[]{lib.toURL()});
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
}
