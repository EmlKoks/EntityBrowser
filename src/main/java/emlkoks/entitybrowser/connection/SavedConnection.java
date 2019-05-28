package emlkoks.entitybrowser.connection;

import emlkoks.entitybrowser.common.Marshaller;
import emlkoks.entitybrowser.resources.Resources;

import javax.xml.bind.annotation.*;
import java.util.*;

/**
 * Created by EmlKoks on 19.03.17.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class SavedConnection {
    @XmlElement(name = "connection")
    private List<Connection> list = new ArrayList<>();

    public List<Connection> getList() {
        return list;
    }

    public void setList(List<Connection> list) {
        this.list = list;
    }

    public void add(Connection sc){
        list.add(sc);
        Marshaller.marshal(this, Resources.SAVED_CONNECTION);
    }

    public Connection getByName(String name) {
        return list.stream()
                .filter(c -> c.getName().equals(name))
                .findFirst().get();
    }

    public void remove(String name){
        for(Connection sc : list){
            if(name.equals(sc.getName())){
                list.remove(sc);
                break;
            }
        }
        Marshaller.marshal(this, Resources.SAVED_CONNECTION);
    }

    public Connection getConnection(String name){
        if(Resources.isNullOrEmpty(name)) return null;
        for(Connection d : list)
            if(name.equals(d.getName()))
                return d;
        return null;
    }
}
