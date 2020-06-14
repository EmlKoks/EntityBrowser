package emlkoks.entitybrowser;

import emlkoks.entitybrowser.view.ViewFile;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DialogPane;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.junit.Test;
import org.testfx.api.FxAssert;
import org.testfx.framework.junit.ApplicationTest;

import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import static org.testfx.matcher.control.LabeledMatchers.hasText;

public class MainTest extends ApplicationTest{

    @Override
    public void start(final Stage stage) throws Exception {
        ResourceBundle bundle = ResourceBundle.getBundle("lang.lang", new Locale("pl"));
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent root = fxmlLoader.load(getClass().getResource(ViewFile.MAIN_WINDOW.getFile()), bundle);
        stage.setScene(new Scene(root));
        stage.show();
    }

//    @Test //cannot run in github TODO
//    public void testExit() {
//        clickOn("#menuHelp");
//        clickOn("#menuAbout");
//        DialogPane aboutDialog = getTopDialog();
//        FxAssert.verifyThat(aboutDialog.getContentText(), hasText("Test"));
//    }

    private DialogPane getTopDialog() {
        List<Window> windows = robotContext().getWindowFinder().listWindows();
        return (DialogPane) windows.get(windows.size()-1).getScene().getRoot();
    }

//    @Test
//    public void test() {
//        System.out.println("true = " + true);
//    }
}