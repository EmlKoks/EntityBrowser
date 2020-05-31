package emlkoks.entitybrowser.session.entity;

import com.google.common.collect.Sets;
import emlkoks.entitybrowser.common.Resources;
import emlkoks.entitybrowser.session.exception.LibraryFileNotFoundException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EntityLibraryLoader {
    private static Set<String> SUPPORTED_ARCHIVERS = Sets.newHashSet("ear", "war");

    private File file;
    private EntityClassLoader classLoader;
    private Set<ClassDetails> loadedClasses;

    public EntityLibraryLoader(File file) throws LibraryFileNotFoundException {
        if (Objects.isNull(file)) {
            throw new LibraryFileNotFoundException("File is null");
        } else if (!file.exists()) {
            throw new LibraryFileNotFoundException("File not exists");
        }
        this.file = file;
        classLoader = new EntityClassLoader(this.getClass().getClassLoader());
    }

    public Set<ClassDetails> load() {
        loadedClasses = new HashSet<>();
        getLibraryListFromFile(file).stream()
                .peek(this::loadToClassLoader)
                .forEach(this::loadEntityClass);
        return loadedClasses;
    }

    private List<File> getLibraryListFromFile(File library) {
        return isArchive(file) ? unzipLib(library) : Collections.singletonList(library);
    }

    private boolean isArchive(File file) {
        String[] splitedName = file.getName().split("[.]");
        return SUPPORTED_ARCHIVERS.contains(splitedName[splitedName.length - 1]);
    }

    private List<File> unzipLib(File lib) {
        byte[] buffer = new byte[1024];
        File dir = Resources.createDirectory(Resources.CACHE_DIR, lib.getName().substring(0, lib.getName().lastIndexOf(".")), true);
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

    private void loadToClassLoader(File library) {
        classLoader.addLib(library);
    }

    private void loadEntityClass(File file) {
        try {
            ZipFile zip = new ZipFile(file);
            Enumeration<ZipEntry> zipEnum = (Enumeration<ZipEntry>) zip.entries();
            for (ZipEntry entry = zipEnum.nextElement(); zipEnum.hasMoreElements(); entry = zipEnum.nextElement()) {
                if (!entry.isDirectory() && entry.getName().endsWith(".class")
                        && !entry.getName().endsWith("_.class")) {
                    loadClass(entry);
                } else if (entry.getName().endsWith(".jar") || entry.getName().endsWith(".war")) {
                    loadJarOrWar(file, entry);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadClass(ZipEntry entry) {
        String classPath = entry.getName().replace("WEB-INF/", "").replace("classes/", "");
        String className = classPath.replace('/', '.').replace(".class", "");
        try {
            Class clazz = Class.forName(className, true, classLoader);
            ClassDetails classDetails = new ClassDetails(clazz);
            if (classDetails.isEntity()) {
                loadedClasses.add(classDetails);
            }
        } catch (ClassNotFoundException | LinkageError | ArrayStoreException e) { }
    }

    private void loadJarOrWar(File file, ZipEntry entry) throws IOException {
        ZipFile zf = new ZipFile(file);
        InputStream entryIs = zf.getInputStream(entry);
        String fileName = "temp/" + new Date().getTime();
        Resources.createDirectory("temp", false);
        File newFile = new File(fileName);
        Files.copy(entryIs, newFile.toPath());
        newFile.delete();
    }
 }
