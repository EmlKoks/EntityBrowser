package emlkoks.entitybrowser.connection;

import java.util.ArrayList;
import java.util.List;

public enum Provider {
    Hibernate,
    EclipseLink;

    public static List<String> getStringValues(){
        List<String> values = new ArrayList<>();
        for(Provider val : Provider.values()){
            values.add(val.toString());
        }
        return values;
    }

}