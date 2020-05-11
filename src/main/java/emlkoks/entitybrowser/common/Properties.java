package emlkoks.entitybrowser.common;

import java.io.IOException;
import java.io.InputStream;

public class Properties {
    java.util.Properties properties = new java.util.Properties();

    public Properties() {
        try (InputStream is = Properties.class.getClassLoader().getResourceAsStream("app.properties")) {
            properties.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getVersion() {
        return "v" + properties.getProperty("version");
    }
}
