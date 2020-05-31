package emlkoks.entitybrowser.view.controller.main;

import emlkoks.entitybrowser.Main;
import emlkoks.entitybrowser.Mode;
import emlkoks.entitybrowser.connection.Connection;
import emlkoks.entitybrowser.mocked.MockSession;
import emlkoks.entitybrowser.query.comparator.ComparatorManager;
import emlkoks.entitybrowser.query.comparator.ComparatorNotFoundException;
import emlkoks.entitybrowser.session.Session;
import emlkoks.entitybrowser.view.ViewFile;
import emlkoks.entitybrowser.view.controller.ChooseConnectionController;
import emlkoks.entitybrowser.view.dialog.InformationDialogCreator;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by EmlKoks on 18.03.17.
 */
@Slf4j
public class MainWindowController implements Initializable {

    @FXML private SplitPane centerContent;
    @FXML private ResourceBundle resources;
    @FXML private AnchorPane leftContent;
    @FXML private AnchorPane rightContent;
    @FXML private ResultsTableController resultsController;
    @FXML SearchController searchController;

    private Session session;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Main.setMainController(this);
        this.resources = resources;
        searchController.initialize(resources, this, leftContent);
        resultsController.initialize(resources, rightContent);
        if (Mode.DEBUG.equals(Main.mode)) {
//            debugWithMock();
//            debugNewSession();
            debugOsp();
//            debugResultsList();
        }

//        Platform.runLater(Updater::new);//TODO run not blocking main thread
    }


    //TODO validate in background
    private static void validateEntities(Session session) {
        session.getClassNames().stream()
                .map(clazz -> session.getEntity(clazz))
                .forEach(entity -> entity.getFields().stream()
                    .forEach(field -> {
                        try {
                            ComparatorManager.getExpressionByField(field);
                        } catch (ComparatorNotFoundException e) {
                            System.out.println(e.getMessage());
                        }
                    }));
    }

    @FXML
    public void exit() {
        Platform.exit();
    }

    @FXML
    public void showAbout() {
        new InformationDialogCreator("About", "Test").show();
    }

    private void debugOsp() {
        Connection connection = Main.savedConnections.getConnections()
                .filtered(c -> "OSP".equals(c.getName()))
                .get(0);
        session = new Session(connection);
        if (session.connect()) {
            openSession(session);
//            validateEntities(session);
        }
    }

    @FXML
    public void createNewSession() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(ViewFile.CHOOSE_CONNECTION.getFile()), resources);
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage stage = openChooseConnectionStage(loader);
        stage.showAndWait();
        openSession(((ChooseConnectionController)loader.getController()).getSession());
    }

    public void openSession(Session session) {
        if (Objects.isNull(session)) {
            return;
        }
        this.session = session;
        updateView();
    }

    private void updateView() {
        searchController.updateSession(session);
        centerContent.setDisable(false);
    }

    private Stage openChooseConnectionStage(FXMLLoader loader) {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        Scene dialogScene = new Scene(loader.getRoot());
        stage.setTitle(resources.getString("chooseConnection.title"));
        stage.setScene(dialogScene);
        return stage;
    }

    public ResultsTableController getResultsController() {
        return resultsController;
    }

//    private void debugResultsList() {
//        List<Connection> results = new ArrayList<>();
//        results.add(new Connection());
//        resultsController.showResults(results, new EntityDetails(Connection.class));
//    }

    private void debugNewSession() {
        createNewSession();
    }

    private void debugWithMock() {
        session = new MockSession();
        openSession(session);
        searchController.doSearch();
    }
}
