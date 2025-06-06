package startskate;


import javafx.application.Application;
import javafx.stage.Stage;
import utils.WindowManager;

import java.io.IOException;


public class FirstMainApp extends Application {

    public void start(Stage primaryStage) throws IOException {
        WindowManager windowManager = WindowManager.getInstance();
        windowManager.setStage(primaryStage);
        windowManager.loadScene("viewFxml/AccessView.fxml");
    }

    public static void main(String[] args) {
        launch();
    }
}
