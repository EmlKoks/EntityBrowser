package emlkoks.entitybrowser.session;

import emlkoks.entitybrowser.Util;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

/**
 * Created by EmlKoks on 03.04.17.
 */
public class EntityList {
    Map<String, Entity> classList = new TreeMap<>();

    private void loadOnlyEntities(File file) {
//        System.out.println("file.getName() = " + file.getName());
        try {
            ZipFile zip = new ZipFile(file);
            Enumeration<ZipEntry> zipEnum = (Enumeration<ZipEntry>) zip.entries();
            for (ZipEntry entry = zipEnum.nextElement(); zipEnum.hasMoreElements(); entry = zipEnum.nextElement()) {
                if (!entry.isDirectory() && entry.getName().endsWith(".class") && !entry.getName().endsWith("_.class")) {
                    String className = entry.getName().replace("WEB-INF/", "").replace("classes/", "");
                    className = className.replace('/', '.').replace(".class", "");
//                    System.out.println("className = " + className);
                    try {
//                        System.out.println("className = " + className);
                        Class clazz = Class.forName(className);
                        if(clazz.getAnnotation(javax.persistence.Entity.class) != null) {
                            classList.put(className.substring(className.lastIndexOf(".") + 1),
                                    new Entity(clazz));
                        }
                    } catch (ClassNotFoundException|NoClassDefFoundError|UnsatisfiedLinkError e) {
//                        e.printStackTrace();
                    } catch (IllegalStateException e) {
                        e.printStackTrace();
                    }
                } else if(entry.getName().endsWith(".jar") || entry.getName().endsWith(".war")){
                    ZipFile zf = new ZipFile(file);
                    InputStream entryIs = zf.getInputStream(entry);
                    String fileName = "temp/" + new Date().getTime();
                    if(!new File("temp").exists()){
                        new File("temp").mkdir();
                    }
                    File newFile = new File(fileName);
                    Files.copy(entryIs, newFile.toPath());
                    loadOnlyEntities(newFile);
                    newFile.delete();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadEntities(File file) {
        System.out.println(new Date().toString());
        loadLib(file);
        loadOnlyEntities(file);
        System.out.println(new Date().toString());
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
//        System.out.println("lib.getName() = " + lib.getName());
        if(lib.getName().toLowerCase().endsWith(".ear")){
            unzipLib(lib);
        }
        Method method;
        try {
            method = URLClassLoader.class.getDeclaredMethod("addURL", new Class[]{URL.class});
            method.setAccessible(true);
//            System.out.println("lib.toURL().toString() = " + lib.toURL().toString());
            method.invoke(ClassLoader.getSystemClassLoader(), new Object[]{lib.toURL()});
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private void unzipLib(File lib){
        byte[] buffer = new byte[1024];
        File dir = new File(Util.cacheDir, lib.getName().substring(0, lib.getName().lastIndexOf(".")));
        if(dir.exists()){
            try {
                FileUtils.deleteDirectory(dir);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        dir.mkdirs();
        try(ZipInputStream zis = new ZipInputStream(new FileInputStream(lib))) {
            ZipEntry ze;
            while ((ze = zis.getNextEntry()) != null) {
                System.out.format("File: %s Size: %d Last Modified %s %n", ze.getName(), ze.getSize(), LocalDate.ofEpochDay(ze.getTime() / 86400000L));
                String fileName = ze.getName();
                File file = new File(dir, fileName);
                if(ze.isDirectory()) {
                    file.mkdirs();
                } else {
                    if(fileName.lastIndexOf("/") > 0) {
                        String parentDirName = fileName.substring(0, fileName.lastIndexOf("/"));
                        File parentDir = new File(dir, parentDirName);
                        if (!parentDir.exists()) parentDir.mkdirs();
                    }
                    file.createNewFile();
                    FileOutputStream fos = new FileOutputStream(file);
                    int len;
                    while ((len = zis.read(buffer)) > 0) {
                        fos.write(buffer, 0, len);
                    }
                    fos.close();
                    if(fileName.toLowerCase().endsWith(".jar") || fileName.toLowerCase().endsWith(".war")){
                        loadLib(file);
                        loadOnlyEntities(file);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
