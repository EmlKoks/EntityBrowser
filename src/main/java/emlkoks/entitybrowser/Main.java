package emlkoks.entitybrowser;

import emlkoks.entitybrowser.connection.SavedConnection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.*;
import java.util.*;

public class Main extends Application {
    static String driverClass = "com.mysql.jdbc.Driver.class";
    static String slang = "lang/pl_Polski.properties";
    public static Properties lang = new Properties();
    public static Map<String, SavedConnection> savedConnections = new HashMap<>();

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
        initialize();
//        loadPropierties();
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
        SavedConnection newsc = new SavedConnection();
        newsc.setName("Test");
        savedConnections.put(newsc.getName(), newsc);
        SavedConnection newsc2 = new SavedConnection();
        newsc2.setName("Test2");
        savedConnections.put(newsc2.getName(), newsc2);
    }


}
