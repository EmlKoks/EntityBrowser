package emlkoks.entitybrowser.controllers;

import emlkoks.entitybrowser.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by EmlKoks on 18.03.17.
 */
public class MainWindowController implements Initializable{

    @FXML
    private BorderPane mainPane;

    @FXML
    private ChoiceBox<String> entityList;

    @FXML
    private ResourceBundle resources;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Main.setMainController(this);
        this.resources=resources;
    }

    @FXML
    private void newConnection(){
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/view/newConnection.fxml"), resources);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene dialogScene = new Scene(root);
        dialog.setTitle(resources.getString("newConnection.title"));
        dialog.setScene(dialogScene);
        dialog.show();
    }

    public void addTab(EntityManagerFactory emf){
        File lib = chooseEntityLib();
        Main.entityList.loadEntities(lib);
        entityList.getItems().addAll(Main.entityList.getClassNames());
        EntityManager em = emf.createEntityManager();
        Class user = Main.entityList.getClass("User");
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery(user);
        cq.from(user);
        List list = em.createQuery(cq).getResultList();
        System.out.println("wait");
    }

    private File chooseEntityLib(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("/mnt/dysk/Programowanie/koks312-adressbook-a508f653c32e/model/target"));
        FileChooser.ExtensionFilter exFilter = new FileChooser.ExtensionFilter(resources.getString("choose.lib_filter"), "*.jar");
        fileChooser.setSelectedExtensionFilter(exFilter);
        File file = fileChooser.showOpenDialog(mainPane.getScene().getWindow());
        return file;
    }
}
