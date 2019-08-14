package emlkoks.entitybrowser;

import emlkoks.entitybrowser.common.Marshaller;
import emlkoks.entitybrowser.common.Properties;
import emlkoks.entitybrowser.connection.DriverList;
import emlkoks.entitybrowser.connection.SavedConnection;
import emlkoks.entitybrowser.resources.Resources;
import emlkoks.entitybrowser.view.controller.MainWindowController;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Queue;
import java.util.ResourceBundle;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application {
    static Properties properties;
    public static SavedConnection savedConnections = new SavedConnection();
    public static DriverList drivers = new DriverList();
    private static MainWindowController mainController;
    private static Queue<Object> entityDetailsQueue = new LinkedList<>();
    public static ResourceBundle bundle;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(final Stage primaryStage) throws Exception {
        bundle = ResourceBundle.getBundle("lang.lang", new Locale("pl"));
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent root = fxmlLoader.load(getClass().getResource("/view/mainWindow.fxml"), bundle);
        primaryStage.setTitle("Entity Browser");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    @Override
    public void init() {
        drivers = Marshaller.unmarshal(DriverList.class, Resources.DRIVERS);
        savedConnections = Marshaller.unmarshal(SavedConnection.class, Resources.SAVED_CONNECTION);
//        properties = new Properties();
    }


    public static MainWindowController getMainController() {
        return mainController;
    }

    public static void setMainController(MainWindowController mainController) {
        Main.mainController = mainController;
    }

    public static void addEntity(Object o) {
        entityDetailsQueue.add(o);
    }

    public static Object getEntity() {
        return entityDetailsQueue.poll();
    }
}
