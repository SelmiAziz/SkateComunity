package view;

import Dao.EventDao;
import Dao.EventDemoDao;
import Dao.Factories.DaoFactory;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Event;
import utils.SceneManager;
import utils.SessionManager;

import java.io.IOException;

public class ManagerHomePageView {

    private Stage stage;



    @FXML
    public void goToEvents() throws IOException {
        // Crea un nuovo evento

        Event newEvent = new Event("skateGo", "Divertiti", "17/04", 20, "Franciia",80);
        Event secondEvent = new Event("Brosku", "vai", "15/03", 15, "Svizzera",90);
        SceneManager sceneManager = SceneManager.getInstance();

        // Ottieni l'istanza del repository e aggiungi l'evento


        // Carica il file FXML per la vista
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ManagerEventsPageView.fxml"));

        // Carica l'FXML e ottieni il root
        Parent root = loader.load();

        // Ottieni il controller dalla vista
        ManagerEventsPageView managerEventsPageView = loader.getController();

        // Imposta la scena con il root e la dimensione desiderata
        Scene scene = new Scene(root, 1200, 800);
        stage = SceneManager.getInstance().getStage();
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();

        // Esegui loadEvents() dopo che la scena è stata completamente visualizzata
        Platform.runLater(() -> managerEventsPageView.loadEvents());
    }

    public void logOut() throws IOException {
        SessionManager.getInstance().logout();
        SceneManager.getInstance().loadScene("AccessView.fxml");
    }
}
