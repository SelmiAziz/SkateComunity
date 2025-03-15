package startSkate;


import javafx.application.Application;
import javafx.stage.Stage;
import utils.SceneManager;

import java.io.IOException;

public class mainApp extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        SceneManager sceneManager = SceneManager.getInstance();
        sceneManager.setStage(primaryStage); // Inizializza lo stage
        sceneManager.loadScene("viewFxml/AccessView.fxml");
    }

    public static void main(String[] args) {
        launch();
    }
}
