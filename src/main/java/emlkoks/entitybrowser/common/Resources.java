package emlkoks.entitybrowser.common;

import emlkoks.entitybrowser.Main;
import emlkoks.entitybrowser.connection.DriverList;
import emlkoks.entitybrowser.connection.SavedConnections;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by koks on 10.03.17.
 */
@Slf4j
public class Resources {
    public static final String CACHE_DIR;
    public static final String CONF_DIR = "conf";
    public static final String CONF_DIR_PATH;
    public static final String DRIVERS = "drivers.xml";
    public static final String DRIVERS_PATH;
    public static final String DRIVERS_DIR = "drivers";
    public static final String DRIVERS_DIR_PATH;
    public static final String DRIVERS_DEFAULT_PATH;
    public static final String HOME_DIR = ".entityBrowser";
    public static final String HOME_DIR_PATH;
    public static final String SAVED_CONNECTIONS = "savedConnections.xml";
    public static final String SAVED_CONNECTIONS_PATH;

    static {
        HOME_DIR_PATH = getHomeDir() + File.separator + HOME_DIR;
        CONF_DIR_PATH = HOME_DIR_PATH + File.separator + CONF_DIR;
        createHomeDirs();
        CACHE_DIR = HOME_DIR_PATH + File.separator + "cache";
        DRIVERS_PATH = HOME_DIR_PATH + File.separator + CONF_DIR + File.separator + DRIVERS;
        DRIVERS_DIR_PATH = HOME_DIR_PATH + File.separator + DRIVERS_DIR + File.separator;
        DRIVERS_DEFAULT_PATH = File.separator + CONF_DIR + File.separator + DRIVERS;
        SAVED_CONNECTIONS_PATH = HOME_DIR_PATH + File.separator + CONF_DIR + File.separator + SAVED_CONNECTIONS;
    }

    public static String getHomeDir() {
        return System.getProperty("user.home");
    }

    private static void createHomeDirs() {
        File homeDir = new File(HOME_DIR_PATH);
        if (!homeDir.exists()) {
            homeDir.mkdirs();
        }
        File confDir = new File(CONF_DIR_PATH);
        if (!confDir.exists()) {
            confDir.mkdirs();
        }

    }

    public static void printClasses(File driver) throws IOException {
        List<String> classNames = new ArrayList<String>();
        ZipInputStream zip = new ZipInputStream(new FileInputStream(driver));
        for (ZipEntry entry = zip.getNextEntry(); entry != null; entry = zip.getNextEntry()) {
            if (!entry.isDirectory() && entry.getName().endsWith(".class")) {
                // This ZipEntry represents a class. Now, what class does it represent?
                String className = entry.getName().replace('/', '.'); // including ".class"
                classNames.add(className.substring(0, className.length() - ".class".length()));
                log.debug("className = " + className);
            }
        }
    }

    public static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }

    public static void loadResources(Class mainClass) {
        loadDrivers(mainClass);
        loadSavedConnections();
    }

    private static void loadDrivers(Class mainClass) {
        Main.drivers = Marshaller.unmarshalOrNull(DriverList.class, DRIVERS_PATH);
        System.out.println("drivers " + Main.drivers);
        if (Objects.isNull(Main.drivers)) {
            try {
                Path source = new File(mainClass.getResource(DRIVERS_DEFAULT_PATH).getFile()).toPath();
                System.out.println("source = " + source);
                Path destination = new File(DRIVERS_PATH).toPath();
                System.out.println("destination = " + destination);
                Files.copy(source, destination, StandardCopyOption.COPY_ATTRIBUTES);
                Main.drivers = Marshaller.unmarshal(DriverList.class, DRIVERS_PATH);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void loadSavedConnections() {
        Main.savedConnections = Marshaller.unmarshal(SavedConnections.class, SAVED_CONNECTIONS_PATH);
        Main.savedConnections.setupConnectionLastId();
    }
}
