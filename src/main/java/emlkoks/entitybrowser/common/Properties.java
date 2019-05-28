package emlkoks.entitybrowser.common;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class Properties {
    java.util.Properties lang = new java.util.Properties();
    String slang = "lang/pl_Polski.properties";

    public Properties(){
        try {
            InputStream is = new FileInputStream(slang);
            lang.load(is);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
