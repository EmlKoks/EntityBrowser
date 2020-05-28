package emlkoks.entitybrowser.common;

import emlkoks.entitybrowser.session.entity.EntityDetails;

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
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;


/**
 * Created by EmlKoks on 08.10.17.
 */
@Slf4j
public class LibraryManager {

    public static Map<String, EntityDetails> getEntitesFromLib(File lib) {
        Map<String, EntityDetails> classMap = new TreeMap<>();
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
     *  Get connections of library from lib.
     * @param library TODO
     * @return - library connections with persistance
     */
    private static List<File> getLibraryListFromFile(File library) {
        List<File> fileList;
        if (library.getName().toLowerCase().endsWith(".ear") || library.getName().toLowerCase().endsWith(".war")) {
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

    private static Map<String, EntityDetails> loadEntityClass(File file) {
        Map<String, EntityDetails> classMap = new TreeMap<>();
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

    private static void loadClass(ZipEntry entry, Map<String, EntityDetails> classMap) {
        String classPath = entry.getName().replace("WEB-INF/", "").replace("classes/", "");
        String className = classPath.replace('/', '.').replace(".class", "");
        try {
            Class clazz = Class.forName(className);
            if (clazz.getAnnotation(javax.persistence.Entity.class) != null) {
                EntityDetails entity = new EntityDetails(clazz);
                classMap.put(className.substring(className.lastIndexOf(".") + 1), entity);
            }
        } catch (ClassNotFoundException | LinkageError | ArrayStoreException e) { }
    }

    private static void loadJarOrWar(File file, ZipEntry entry, Map<String, EntityDetails> classMap)
            throws IOException {
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
 }
