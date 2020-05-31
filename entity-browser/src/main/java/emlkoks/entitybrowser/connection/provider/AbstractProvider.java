package emlkoks.entitybrowser.connection.provider;

import emlkoks.entitybrowser.connection.Property;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;

public abstract class AbstractProvider {
    @Getter
    protected final Set<Property> defaultProperties = new HashSet<>();


}
