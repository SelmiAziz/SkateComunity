package startSkate;


import javafx.application.Application;
import javafx.stage.Stage;
import utils.WindowManager;
import utils.WindowManagerBasic;

import java.io.IOException;

public class MainProva extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        WindowManagerBasic windowManagerBasic = WindowManagerBasic.getInstance();
        windowManagerBasic.setStage(primaryStage);
        windowManagerBasic.loadScene("viewFxmlBasic/LogPageViewBasic.fxml");
    }

    public static void main(String[] args) {
        launch();
    }
}
