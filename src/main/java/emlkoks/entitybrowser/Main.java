package emlkoks.entitybrowser;

import emlkoks.entitybrowser.common.Marshaller;
import emlkoks.entitybrowser.common.Properties;
import emlkoks.entitybrowser.connection.DriverList;
import emlkoks.entitybrowser.connection.SavedConnections;
import emlkoks.entitybrowser.common.Resources;
import emlkoks.entitybrowser.view.ViewFile;
import emlkoks.entitybrowser.view.controller.main.MainWindowController;
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
    public static Mode mode = Mode.PROD;
    public static Properties properties;
    public static SavedConnections savedConnections = new SavedConnections();
    public static DriverList drivers = new DriverList();
    private static MainWindowController mainController;
    private static Queue<Object> entityDetailsQueue = new LinkedList<>();
    public static ResourceBundle resources;

    public static void main(String[] args) {
        if  (args.length > 0) {
            mode = Mode.valueOf(args[0]);
        }
        launch(args);
    }

    @Override
    public void init() {
        drivers = Marshaller.unmarshal(DriverList.class, Resources.DRIVERS);
        savedConnections = Marshaller.unmarshal(SavedConnections.class, Resources.SAVED_CONNECTION);
        properties = new Properties();
    }

    @Override
    public void start(final Stage primaryStage) throws Exception {

        resources = ResourceBundle.getBundle("lang.lang", new Locale("pl"));
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent root = fxmlLoader.load(getClass().getResource(ViewFile.MAIN_WINDOW.getFile()), resources);
        primaryStage.setTitle("Entity Browser");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

//        if (Mode.DEBUG.equals(mode)) {
//            initDebugData();
//        }
    }

    private void initDebugData() {
        System.out.println("debug");
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
