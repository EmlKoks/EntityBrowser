package emlkoks.entitybrowser.view.controller.main;

import emlkoks.entitybrowser.view.ViewFile;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.testfx.framework.junit.ApplicationTest;


public class MainWindowControllerTest  extends ApplicationTest {
    private MainWindowController controller;

    @Override
    public void start(final Stage stage) throws Exception {
        ResourceBundle bundle = ResourceBundle.getBundle("lang.lang", new Locale("pl"));
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent root = fxmlLoader.load(getClass().getResource(ViewFile.MAIN_WINDOW.getFile()), bundle);
        controller = fxmlLoader.getController();
        stage.setScene(new Scene(root));
        stage.show();
    }
    
//    @Test
//    public void openSession() {
//        var session = new Session(null);
//        controller.openSession(session);
//        System.out.println("session = " + session);
//    }

}