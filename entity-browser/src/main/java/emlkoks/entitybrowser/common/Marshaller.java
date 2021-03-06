package emlkoks.entitybrowser.common;

import java.io.File;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

public class Marshaller {

    public static void marshal(Object obj, String fileName) {
        try {
            File file = new File(fileName);
            JAXBContext jaxbContext = JAXBContext.newInstance(obj.getClass());
            javax.xml.bind.Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(javax.xml.bind.Marshaller.JAXB_FORMATTED_OUTPUT, true);
            jaxbMarshaller.marshal(obj, file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static <T> T unmarshal(Class<T> clazz, String fileName) {
        System.out.println("fileName = " + fileName);
        try {
            File file = new File(fileName);
            JAXBContext jaxbContext = JAXBContext.newInstance(clazz);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            return clazz.cast(jaxbUnmarshaller.unmarshal(file));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T unmarshalOrNull(Class<T> clazz, String fileName) {
        System.out.println("fileName = " + fileName);
        try {
            File file = new File(fileName);
            JAXBContext jaxbContext = JAXBContext.newInstance(clazz);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            return clazz.cast(jaxbUnmarshaller.unmarshal(file));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
