package view;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import utils.SceneManager;

import java.awt.*;
import java.io.IOException;

public class OrganizerHomePageView {

    private Stage stage;
    SceneManager sceneManager = SceneManager.getInstance();
    @FXML private Label errorLabel;

    @FXML
    public void goToEvents() {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/OrganizerEventsPageView.fxml"));

        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            System.err.println("Errore di I/O: " + e.getMessage());
            errorLabel.setText(e.getMessage());
        }

        OrganizerEventsPageView organizerEventsPageView = loader.getController();

        Scene scene = new Scene(root, 1200, 800);
        stage = SceneManager.getInstance().getStage();
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
        Platform.runLater(() -> organizerEventsPageView.loadEvents());
    }

    public void logOut()  {
        //here you have to call the controller to logOut
        try {
            sceneManager.loadScene("AccessView.fxml");
        }catch(IOException e){
            System.err.println("Errore di I/O: " + e.getMessage());
            errorLabel.setText(e.getMessage());
        }
    }
}
