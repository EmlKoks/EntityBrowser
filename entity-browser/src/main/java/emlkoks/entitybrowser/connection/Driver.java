package emlkoks.entitybrowser.connection;

import emlkoks.entitybrowser.common.CustomClassLoader;
import emlkoks.entitybrowser.common.Resources;
import java.io.File;
import java.util.Objects;
import javax.xml.bind.annotation.XmlTransient;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by koks on 10.03.17.
 */
@Getter
@Setter
@NoArgsConstructor
public class Driver {
    private String name;
    private String libraryPath;
    private String className;
    private String urlTemplate;
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @XmlTransient
    private boolean wasLoaded = false;

    public void loadDriver() {
        if (wasLoaded) {
            return;
        }
        var classLoader = new CustomClassLoader(this.getClass().getClassLoader());
        classLoader.addLib(getLibraryFile());
        wasLoaded = true;
    }

    private File getLibraryFile() {
        String driverUrl = Resources.DRIVERS_DIR_PATH + libraryPath;
        File driverFile = new File(driverUrl);
        if (!driverFile.exists()) {
            throw new DriverNotFoundException("Cannot find driver file " + driverUrl);
        }
        return driverFile;
    }

    public boolean equalsName(String name) {
        return Objects.nonNull(this.name) && this.name.equals(name);
    }
}
