package emlkoks.entitybrowser.session.entity;

import com.google.common.collect.Sets;
import emlkoks.entitybrowser.common.Resources;
import emlkoks.entitybrowser.session.exception.LibraryFileNotFoundException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EntityLibraryLoader {
    private static Set<String> SUPPORTED_ARCHIVERS = Sets.newHashSet("ear", "war", "jar");

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
        unzipLib(file).stream()
                .peek(classLoader::addLib)
                .forEach(this::loadEntities);
        return loadedClasses;
    }

    private Set<File> unzipLib(File library) {
        Set<File> libraryFiles = Sets.newHashSet(library);
        String directoryName = library.getName().substring(0, library.getName().lastIndexOf("."));
        File directory = Resources.createDirectory(Resources.CACHE_DIR, directoryName, true);
        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(library))) {
            for (ZipEntry ze = zis.getNextEntry(); ze != null; ze = zis.getNextEntry()) {
                if (!ze.isDirectory() && isArchive(ze.getName())) {
                    File file = Resources.createFile(directory, ze.getName());
                    writeZipEntryToFile(zis, file);
                    libraryFiles.addAll(unzipLib(file));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return libraryFiles;
    }

    private boolean isArchive(String fileName) {
        String[] splitedName = fileName.split("[.]");
        return SUPPORTED_ARCHIVERS.contains(splitedName[splitedName.length - 1]);
    }

    private void writeZipEntryToFile(ZipInputStream zipInputStream, File file) throws IOException {
        byte[] buffer = new byte[1024];
        try (FileOutputStream fos = new FileOutputStream(file)) {
            int len;
            while ((len = zipInputStream.read(buffer)) > 0) {
                fos.write(buffer, 0, len);
            }
        }
    }

    private void loadEntities(File file) {
        try {
            ZipFile zip = new ZipFile(file);
            zip.stream()
                    .map(ZipEntry::getName)
                    .filter(this::isClass)
                    .filter(this::isNotExcludedClass)
                    .forEach(this::loadClass);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean isClass(String fileName) {
        return fileName.endsWith(".class");
    }

    private boolean isNotExcludedClass(String fileName) {
        return !fileName.endsWith("_.class");
    }

    private void loadClass(String filePath) {
        String classPath = filePath.replace("WEB-INF/", "").replace("classes/", "");
        String className = classPath.replace('/', '.').replace(".class", "");
        try {
            Class clazz = Class.forName(className, true, classLoader);
            ClassDetails classDetails = new ClassDetails(clazz);
            if (classDetails.isEntity()) {
                loadedClasses.add(classDetails);
            }
        } catch (ClassNotFoundException | LinkageError | ArrayStoreException e) {
            log.info("Cannot load class {}", className);
        }
    }
 }
