package emlkoks.entitybrowser.connection;

import emlkoks.entitybrowser.Main;
import emlkoks.entitybrowser.Util;

import javax.xml.bind.annotation.*;
import java.util.*;

/**
 * Created by EmlKoks on 19.03.17.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class SavedConnectionList{
    @XmlElement(name = "savedConnection")
    private List<SavedConnection> list = new ArrayList<>();

    public List<SavedConnection> getList() {
        return list;
    }

    public void setList(List<SavedConnection> list) {
        this.list = list;
    }

    public void add(SavedConnection sc){
        list.add(sc);
        Main.marshal(this, Util.savedConnection);
    }

    public void remove(String name){
        for(SavedConnection sc : list){
            if(name.equals(sc.getName())){
                list.remove(sc);
                break;
            }
        }
        Main.marshal(this, Util.savedConnection);
    }
}
