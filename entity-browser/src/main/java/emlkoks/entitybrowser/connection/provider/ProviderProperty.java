package emlkoks.entitybrowser.connection.provider;

import lombok.Getter;

@Getter
public enum  ProviderProperty {
    DRIVER("javax.persistence.jdbc.driver"),
    PASSWORD("javax.persistence.jdbc.password"),
    USER("javax.persistence.jdbc.user"),
    URL("javax.persistence.jdbc.url");

    private String name;

    ProviderProperty(String name) {
        this.name = name;
    }
}
