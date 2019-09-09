package emlkoks.entitybrowser.connection.provider;

import emlkoks.entitybrowser.connection.Property;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

public abstract class AbstractProvider {
    @Getter
    protected final Set<Property> defaultProperties = new HashSet<>();


}
