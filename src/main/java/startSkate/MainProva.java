package startSkate;


import javafx.application.Application;
import javafx.stage.Stage;
import utils.WindowManager;

import java.io.IOException;

public class MainProva extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        WindowManager sceneManager = WindowManager.getInstance();
        sceneManager.setStage(primaryStage);
        sceneManager.loadScene("viewFxmlBasic/LogPageBasicViewBasic.fxml");
    }

    public static void main(String[] args) {
        launch();
    }
}
