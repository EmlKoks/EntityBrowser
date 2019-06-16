package emlkoks.entitybrowser.common;

import emlkoks.entitybrowser.resources.Resources;
import emlkoks.entitybrowser.session.Entity;
import emlkoks.entitybrowser.session.FieldProperty;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
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
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import javax.persistence.Transient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;


/**
 * Created by EmlKoks on 08.10.17.
 */
@Slf4j
public class LibraryManager {

    public static Map<String, Entity> getEntitesFromLib(File lib) {
        Map<String, Entity> classMap = new TreeMap<>();
        List<File> libWithClassToLoad = getLibraryListFromFile(lib);
        libWithClassToLoad.forEach(f ->
            classMap.putAll(loadEntityClass(f))
        );
        return classMap;
    }

    private static void removeDirectoryIfExists(File dir) {
        if (dir.exists()) {
            try {
                FileUtils.deleteDirectory(dir);
            } catch (IOException e) {
                log.error("Cannot remove directory", e);
                e.printStackTrace();
            }
        }
    }

    /**
     * Unzip file to temp directory.
     * @param lib TODO
     */
    private static List<File> unzipLib(File lib) {
        byte[] buffer = new byte[1024];
        File dir = new File(Resources.CACHE_DIR, lib.getName().substring(0, lib.getName().lastIndexOf(".")));
        removeDirectoryIfExists(dir);
        dir.mkdirs();
        List<File> unzippedFiles = new ArrayList<>();
        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(lib))) {
            ZipEntry ze = zis.getNextEntry();
            while (ze != null) {
                String fileName = ze.getName();
                File file = new File(dir, fileName);
                if (ze.isDirectory()) {
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
                    if (fileName.toLowerCase().endsWith(".jar") || fileName.toLowerCase().endsWith(".war")) {
                        getLibraryListFromFile(file);
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
     *  Get list of library from lib.
     * @param library TODO
     * @return - library list with persistance
     */
    private static List<File> getLibraryListFromFile(File library) {
        List<File> fileList;
        if (library.getName().toLowerCase().endsWith(".ear")) {
            fileList = unzipLib(library);
        } else {
            fileList = new ArrayList<>();
            fileList.add(library);
        }
        if (!fileList.isEmpty()) {
            loadLibraryToClassLoader(library);
        }
        return fileList;
    }

    private static void loadLibraryToClassLoader(File library) {
        try {
            Method method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
            method.setAccessible(true);
            method.invoke(ClassLoader.getSystemClassLoader(), library.toURL());
        } catch (ReflectiveOperationException | MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private static Map<String, Entity> loadEntityClass(File file) {
        Map<String, Entity> classMap = new TreeMap<>();
        try {
            ZipFile zip = new ZipFile(file);
            Enumeration<ZipEntry> zipEnum = (Enumeration<ZipEntry>) zip.entries();
            for (ZipEntry entry = zipEnum.nextElement(); zipEnum.hasMoreElements(); entry = zipEnum.nextElement()) {
                if (!entry.isDirectory() && entry.getName().endsWith(".class")
                        && !entry.getName().endsWith("_.class")) {
                    loadClass(entry, classMap);
                } else if (entry.getName().endsWith(".jar") || entry.getName().endsWith(".war")) {
                    loadJarOrWar(file, entry, classMap);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return classMap;
    }

    private static void loadClass(ZipEntry entry, Map<String, Entity> classMap) {
        String classPath = entry.getName().replace("WEB-INF/", "").replace("classes/", "");
        String className = classPath.replace('/', '.').replace(".class", "");
        try {
            log.debug("className = " + className);
            Class clazz = Class.forName(className);
            if (clazz.getAnnotation(javax.persistence.Entity.class) != null) {
                Entity entity = new Entity(clazz);
                entity.setFields(getEntityFields(entity));
                classMap.put(className.substring(className.lastIndexOf(".") + 1), entity);
            }
        } catch (ClassNotFoundException | NoClassDefFoundError | UnsatisfiedLinkError e) {
            //Skip
        }
    }

    private static void loadJarOrWar(File file, ZipEntry entry, Map<String, Entity> classMap) throws IOException {
        ZipFile zf = new ZipFile(file);
        InputStream entryIs = zf.getInputStream(entry);
        String fileName = "temp/" + new Date().getTime();
        if (!new File("temp").exists()) {
            new File("temp").mkdir();
        }
        File newFile = new File(fileName);
        Files.copy(entryIs, newFile.toPath());
        classMap.putAll(loadEntityClass(newFile));
        newFile.delete();
    }

    private static SortedMap<String, FieldProperty> getEntityFields(Entity entity) {
        SortedMap<String, FieldProperty> fields = new TreeMap<>();
        Class clazz = entity.getClazz();
        for (Field field : clazz.getDeclaredFields()) {
            if (field.getAnnotation(Transient.class) != null) {
                continue;
            }
            FieldProperty fp = new FieldProperty(field.getName());
            fp.setField(field);
            fp.setParentClass(entity.getClazz());
            String methodName = field.getName().substring(0,1).toUpperCase() + field.getName().substring(1);
            boolean isBoolean = field.getType() == boolean.class;
            try {
                Method getMethod = clazz.getMethod((isBoolean ? "is" : "get") + methodName);
                fp.setGetMethod(getMethod);
            } catch (NoSuchMethodException e) {
                if ("serialVersionUID".equals(field.getName())) { //TODO do exception name list
                    continue;
                }
                log.debug("Cannot find method " + (isBoolean ? "is" : "get") + methodName
                        + " in class " + clazz.getName());
                continue;
            }
            try {
                Method setMethod = clazz.getMethod("set" + methodName, field.getType());
                fp.setSetMethod(setMethod);
            } catch (NoSuchMethodException e) {
                if ("serialVersionUID".equals(field.getName())) { //TODO do exception name list
                    continue;
                }
                log.debug("Cannot find method get" + methodName + " in class " + clazz.getName());
                continue;
            }
            fields.put(field.getName(), fp);
        }
        return fields;
    }

}
