package startSkate;


import javafx.application.Application;
import javafx.stage.Stage;
import utils.SceneManager;

import java.io.IOException;

public class MainProva extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        SceneManager sceneManager = SceneManager.getInstance();
        sceneManager.setStage(primaryStage);
        sceneManager.loadScene("viewFxmlBasic/LogPageBasicView.fxml");
    }

    public static void main(String[] args) {
        launch();
    }
}
