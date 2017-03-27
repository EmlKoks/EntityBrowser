package emlkoks.entitybrowser;

import emlkoks.entitybrowser.connection.DriverList;
import emlkoks.entitybrowser.connection.SavedConnectionList;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.util.*;

public class Main extends Application {
    static String driverClass = "com.mysql.jdbc.Driver.class";
    static String slang = "lang/pl_Polski.properties";
    public static Properties lang = new Properties();
    public static SavedConnectionList savedConnections = new SavedConnectionList();
    public static DriverList driverList = new DriverList();

    @Override
    public void start(final Stage primaryStage) throws Exception{
        ResourceBundle bundle = ResourceBundle.getBundle("lang.lang", new Locale("pl"));
        Parent root = FXMLLoader.load(getClass().getResource("/view/chooseEntities.fxml"), bundle);
        Scene scene = new Scene(root, 500, 500);


//        sp.getChildren().add(tJarChosed);
        primaryStage.setTitle("Entity Browser");
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        try {
            initialize();
            launch(args);
//        printDrivers();
//        Driver driver = DriverList.getDriver("MySQL");
//        String url = "jdbc:mysql://localhost/adress_book";
//        try {
//            ClassLoader.getSystemClassLoader().loadClass(driver.getClassName());
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }

//        EntityManagerFactory factory = Connector.createConnection(driver, "root", "admin", url);
//        EntityManager em = factory.createEntityManager();
//        List<Object[]> res = em.createNativeQuery("Select * from users").getResultList();
//        System.out.println("res = " + res);
        } catch (Exception e){
            System.out.println("dupa");
        }
    }

    private static void loadPropierties(){
        try {
            InputStream is = new FileInputStream(slang);
            lang.load(is);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void printDrivers(){
        for(File driver : Util.driverList()){
            try {
                Util.printClasses(driver);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void initialize(){
        unmarshal();
//        loadPropierties();
    }

    public static void marshal(){
        try {
            File file = new File(Util.savedConnection);
            JAXBContext jaxbContext = JAXBContext.newInstance(SavedConnectionList.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            jaxbMarshaller.marshal(savedConnections, file);
        } catch (JAXBException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void unmarshal(){
        try {
            File file = new File(Util.savedConnection);
            JAXBContext jaxbContext = JAXBContext.newInstance(SavedConnectionList.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            savedConnections = (SavedConnectionList) jaxbUnmarshaller.unmarshal(file);
        } catch (JAXBException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}