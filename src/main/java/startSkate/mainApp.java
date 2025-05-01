package startSkate;


import javafx.application.Application;
import javafx.stage.Stage;
import utils.WindowManager;

import java.io.IOException;

import static java.lang.Thread.sleep;

public class mainApp extends Application {

    public void start(Stage primaryStage) throws IOException {
        WindowManager sceneManager = WindowManager.getInstance();
        sceneManager.setStage(primaryStage);
        sceneManager.loadScene("viewFxml/AccessView.fxml");
    }

    public static void main(String[] args) {
        launch();
    }
}
