package utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class SceneManager {
    private Stage stage;
    private static SceneManager instanceSceneManager;

    private SceneManager() {} // Costruttore privato

    public static SceneManager getSingletonInstance() {
        if (instanceSceneManager == null) {
            instanceSceneManager = new SceneManager();
        }
        return instanceSceneManager;
    }

    public void setStage(Stage stage) {
        if (this.stage == null) { // Assegna lo stage solo se non è già impostato
            this.stage = stage;
        }
    }

    public void loadScene(String fxmlPath) throws IOException {
        if (stage == null) {
            throw new IllegalStateException("Stage non inizializzato. Chiama setStage(stage) prima di loadScene().");
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/" + fxmlPath));
        Parent root = loader.load();
        Scene scene = new Scene(root, 1200, 800);

        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }
}

