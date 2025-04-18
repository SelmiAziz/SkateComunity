package startSkate;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import utils.SceneManager;
import view.OrganizerCompetitionsPageView;

import java.io.IOException;

import static java.lang.Thread.sleep;

public class mainApp extends Application {

    public void start(Stage primaryStage) throws IOException {
        SceneManager sceneManager = SceneManager.getInstance();
        sceneManager.setStage(primaryStage);
        sceneManager.loadScene("viewFxml/AccessView.fxml");
    }

    public static void main(String[] args) {
        launch();
    }
}
