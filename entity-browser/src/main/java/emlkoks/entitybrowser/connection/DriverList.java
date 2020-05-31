package emlkoks.entitybrowser.connection;

import com.google.common.base.Strings;
import emlkoks.entitybrowser.common.Marshaller;
import emlkoks.entitybrowser.common.Resources;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class DriverList {
    private List<Driver> drivers = new ArrayList<>();

    public Driver getDriver(String name) {
        if (Strings.isNullOrEmpty(name)) {
            return null;
        }
        return drivers.stream()
                .filter(driver -> driver.equalsName(name))
                .findFirst()
                .orElse(null);
    }

    public List<String> getDriverNames() {
        List<String> names = new ArrayList<>();
        drivers.forEach(x -> names.add(x.getName()));
        return names;
    }

    public void add(Driver sc) {
        drivers.add(sc);
        Marshaller.marshal(this, Resources.DRIVERS);
    }
}
