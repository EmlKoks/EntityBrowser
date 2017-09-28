package emlkoks.entitybrowser.connection;

import java.util.ArrayList;
import java.util.List;

public enum ProviderEnum {
    Hibernate,
    EclipseLink;

    public static List<String> getStringValues(){
        List<String> values = new ArrayList<>();
        for(ProviderEnum val : ProviderEnum.values()){
            values.add(val.toString());
        }
        return values;
    }

}