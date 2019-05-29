package emlkoks.entitybrowser.common;

import emlkoks.entitybrowser.resources.Resources;
import emlkoks.entitybrowser.session.Entity;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

/**
 * Created by EmlKoks on 08.10.17.
 */
public class LibraryManager {

    public static Map<String, Entity> getEntitesFromLib(File lib) {
        Map<String, Entity> classMap = new TreeMap<>();
        List<File> libWithClassToLoad = LibraryManager.loadLib(lib);
        libWithClassToLoad.forEach(f ->
            classMap.putAll(loadEntityClass(f))
        );
        return classMap;
    }

    /**
     * Unzip file to temp directory
     * @param lib
     */
    private static List<File> unzipLib(File lib){
        byte[] buffer = new byte[1024];
        File dir = new File(Resources.CACHE_DIR, lib.getName().substring(0, lib.getName().lastIndexOf(".")));
        if(dir.exists()){
            try {
                FileUtils.deleteDirectory(dir);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        dir.mkdirs();
        List<File> unzippedFiles = new ArrayList<>();
        try(ZipInputStream zis = new ZipInputStream(new FileInputStream(lib))) {
            ZipEntry ze = zis.getNextEntry();
            while (ze != null) {
                String fileName = ze.getName();
                File file = new File(dir, fileName);
                if(ze.isDirectory()) {
                    file.mkdirs();
                } else if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                } else {
                    FileOutputStream fos = new FileOutputStream(file);
                    int len;
                    while ((len = zis.read(buffer)) > 0) {
                        fos.write(buffer, 0, len);
                    }
                    fos.close();
                    if(fileName.toLowerCase().endsWith(".jar") || fileName.toLowerCase().endsWith(".war")){
                        loadLib(file);
                        unzippedFiles.add(file);
                    }
                }
                ze = zis.getNextEntry();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return unzippedFiles;
    }

    /**
     *  Load classes from lib to ClassLoader
     * @param lib
     * @return - library list with persistance
     */
    private static List<File> loadLib(File lib){
        List<File> fileList;
        try {
            JarFile jar = new JarFile(lib);
//            jar.getEntry("").
            System.out.println("jar = " + jar);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(lib.getName().toLowerCase().endsWith(".ear")){
            fileList = unzipLib(lib);
        } else {
            fileList = new ArrayList<>();
            fileList.add(lib);
        }
        Method method;
        try {
            method = URLClassLoader.class.getDeclaredMethod("addURL", new Class[]{URL.class});
            method.setAccessible(true);
            method.invoke(ClassLoader.getSystemClassLoader(), new Object[]{lib.toURL()});
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return fileList;
    }

    private static Map<String, Entity> loadEntityClass(File file) {
        Map<String, Entity> classMap = new TreeMap<>();
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
                            classMap.put(className.substring(className.lastIndexOf(".") + 1),
                                    new Entity(clazz));
                        }
                    } catch (ClassNotFoundException|NoClassDefFoundError|UnsatisfiedLinkError e) {
//                        e.printStackTrace();
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
                    classMap.putAll(loadEntityClass(newFile));
                    newFile.delete();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return classMap;
    }

}