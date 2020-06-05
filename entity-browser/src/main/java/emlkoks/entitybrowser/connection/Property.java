package emlkoks.entitybrowser.connection;

import emlkoks.entitybrowser.connection.provider.ProviderProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Property {
    private String name;
    private Object value;

    public Property(ProviderProperty property, Object value) {
        this(property.getName(), value);
    }
}
