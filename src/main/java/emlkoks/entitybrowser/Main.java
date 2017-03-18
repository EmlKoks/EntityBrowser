package emlkoks.entitybrowser;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

public class Main extends Application {
    static String driverClass = "com.mysql.jdbc.Driver.class";
    static String slang = "lang/pl_Polski.properties";
    public static Properties lang = new Properties();

    @Override
    public void start(final Stage primaryStage) throws Exception{
        ResourceBundle bundle = ResourceBundle.getBundle("lang.lang", new Locale("pl"));
        Parent root = FXMLLoader.load(getClass().getResource("/view/chooseEntities.fxml"), bundle);
        final StackPane sp = new StackPane();
        Scene scene = new Scene(root, 500, 500);


//
//        sp.getChildren().add(bJarChooser);
//        sp.getChildren().add(tJarChosed);
        primaryStage.setTitle("Entity Browser");
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
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


}
