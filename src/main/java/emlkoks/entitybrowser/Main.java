package emlkoks.entitybrowser;

import emlkoks.entitybrowser.drivers.Driver;
import emlkoks.entitybrowser.drivers.DriverList;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class Main extends Application {
    static String driverClass = "com.mysql.jdbc.Driver.class";

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }


    public static void main(String[] args) {

//        launch(args);
//        printDrivers();
        Driver driver = DriverList.getDriver("MySQL");
        String url = "jdbc:mysql://localhost/adress_book";
//        try {
//            ClassLoader.getSystemClassLoader().loadClass(driver.getClassName());
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }

        EntityManagerFactory factory = Connector.createConnection(driver, "root", "admin", url);
        EntityManager em = factory.createEntityManager();
        List<Object[]> res = em.createNativeQuery("Select * from users").getResultList();
        System.out.println("res = " + res);
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
