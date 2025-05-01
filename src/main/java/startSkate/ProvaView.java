package startSkate;

import javafx.application.Application;
import javafx.stage.Stage;
import utils.WindowManager;

import java.io.IOException;


public class ProvaView extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {
        WindowManager sceneManager = WindowManager.getInstance();
        sceneManager.setStage(primaryStage); // Inizializza lo stage
        sceneManager.loadScene("viewFxml/CustomerCommissionPageView.fxml");
    }

    public static void main(String[] args) {
        launch();
    }
}
