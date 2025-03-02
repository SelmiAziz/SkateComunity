package view;

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

public class UserHomePageView {
    //DEVI IMPLEMENTARE UNA FUNZIONE SCENE MANAGER CHE PERMETTE DI FARE QUESTO È STUPIDO FARLO IN QUESTO MODO
    private Stage stage;
    @FXML
    public void goToEvents() throws IOException {
        // Crea un nuovo evento

        Event newEvent = new Event("skateGo", "Divertiti", "17/04",20, "Olanda",110, "mattia");
        Event secondEvent = new Event("Brosku", "vai", "15/03", 10, "Messico",40, "francesco");
        SceneManager sceneManager = SceneManager.getInstance();

        // Ottieni l'istanza del repository e aggiungi l'event

        // Carica il file FXML per la vista
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/UserEventsPageView.fxml"));

        // Carica l'FXML e ottieni il root
        Parent root = loader.load();

        // Ottieni il controller dalla vista
        UserEventsPageView userEventsPageView = loader.getController();

        // Imposta la scena con il root e la dimensione desiderata
        Scene scene = new Scene(root, 1200, 800);
        stage = SceneManager.getInstance().getStage();
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();

        // Esegui loadEvents() dopo che la scena è stata completamente visualizzata
        Platform.runLater(() -> userEventsPageView.loadEvents());
    }

    public void logOut() throws IOException {
        SessionManager.getInstance().terminateSession();
        SceneManager.getInstance().loadScene("AccessView.fxml");
    }


}
