package startSkate;

import Dao.EventDemoDao;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Event;
import view.UserEventsPageView;

import java.io.IOException;

public class mainDao extends Application {





    @Override
    public void start(Stage primaryStage) throws IOException {
        // Crea un nuovo evento
        Event newEvent = new Event("skateGo", "Divertiti", "17/04",10,"Germany",100);
        Event secondEvent = new Event("Brosku", "vai", "15/03",20,"Albania",200);

        // Ottieni l'istanza del repository e aggiungi l'evento
        EventDemoDao myRepo = EventDemoDao.getInstance();
        myRepo.addEvent(newEvent);
        myRepo.addEvent(secondEvent);

        // Carica il file FXML per la vista
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/UserEventsPageView.fxml"));

        // Carica l'FXML e ottieni il root
        Parent root = loader.load();

        // Ottieni il controller dalla vista
        UserEventsPageView createEventView = loader.getController();

        // Imposta la scena con il root e la dimensione desiderata
        Scene scene = new Scene(root, 1200, 800);
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();

        // Esegui loadEvents() dopo che la scena è stata completamente visualizzata
        Platform.runLater(() -> createEventView.loadEvents());
    }




    public static void main(String[] args) {
           launch();
       }


}
