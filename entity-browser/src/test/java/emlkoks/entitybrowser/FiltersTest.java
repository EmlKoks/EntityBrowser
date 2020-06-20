package emlkoks.entitybrowser;

import emlkoks.entitybrowser.mocked.MockSession;
import emlkoks.entitybrowser.session.Session;
import emlkoks.entitybrowser.session.exception.LibraryFileNotFoundException;
import emlkoks.entitybrowser.view.ViewFile;
import emlkoks.entitybrowser.view.controller.main.MainWindowController;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.testfx.framework.junit.ApplicationTest;

public class FiltersTest extends ApplicationTest {
    private MainWindowController mainWindowController;

    @Override
    public void start(final Stage stage) throws Exception {
        ResourceBundle bundle = ResourceBundle.getBundle("lang.lang", new Locale("pl"));
        FXMLLoader loader = new FXMLLoader(getClass().getResource(ViewFile.MAIN_WINDOW.getFile()), bundle);
        Parent root = loader.load();
        mainWindowController = loader.getController();
        stage.setScene(new Scene(root));
        stage.show();
    }

//    @Test
    public void createSession() throws LibraryFileNotFoundException {
        Session session = new MockSession();
        mainWindowController.openSession(session);
        System.out.print("asdasd");
    }
}
