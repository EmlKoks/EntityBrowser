package emlkoks.entitybrowser.common;

import com.google.common.base.Strings;
import emlkoks.entitybrowser.Main;
import emlkoks.entitybrowser.connection.DriverList;
import emlkoks.entitybrowser.connection.SavedConnections;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

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
        new File(HOME_DIR_PATH).mkdirs();
        new File(CONF_DIR_PATH).mkdirs();
    }

    public static File createDirectory(String parent, String name, boolean removeExisting) {
        if (Strings.isNullOrEmpty(parent)) {
            return createDirectory(name, removeExisting);
        }
        return createDirectory(parent + File.separator + name, removeExisting);
    }

    public static File createDirectory(String name, boolean removeExisting) {
        File directory = new File(name);
        if (directory.exists() && removeExisting) {
            log.debug("Directory to remove {} exists.", name);
            try {
                FileUtils.deleteDirectory(directory);
            } catch (IOException e) {
                log.error("Cannot remove directory", e);
                e.printStackTrace();
            }
        }
        if (directory.mkdirs()) {
            log.debug("File {} succesfull created.", name);
        } else {
            log.debug("File {} not created.", name);
        }
        return directory;
    }

    public static File createFile(File parent, String name) throws IOException {
        File file = new File(parent, name);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        file.createNewFile();
        return file;
    }

    public static void loadResources(Class mainClass) {
        loadDrivers(mainClass);
        loadSavedConnections();
    }

    private static void loadDrivers(Class mainClass) {
        Main.drivers = Marshaller.unmarshalOrNull(DriverList.class, DRIVERS_PATH);
        if (Objects.isNull(Main.drivers)) {
            try {
                Path source = new File(mainClass.getResource(DRIVERS_DEFAULT_PATH).getFile()).toPath();
                Path destination = new File(DRIVERS_PATH).toPath();
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
