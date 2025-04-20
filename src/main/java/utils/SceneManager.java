package utils;

import beans.DeliveryDestinationBean;
import beans.BoardBean;
import controls.CustomOrderController;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;
import view.CustomerMakeOrdersPageView;

import java.io.IOException;

public class SceneManager {
    private Stage stage;
    private Stage stageBr;
    private static SceneManager instance;

    private SceneManager() {} // Costruttore privato

    //I didn't make it synchronized
    public synchronized static SceneManager getInstance() {
        if (instance == null) {
            instance = new SceneManager();
        }
        return instance;
    }

    public void setStage(Stage stage) {
        if (this.stage == null) {
            this.stage = stage;
        }
    }

    public Stage getStage(){
        return this.stage;
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

    public void loadMakeOrdersPage( CustomOrderController controller, BoardBean skateboardBean) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/viewFxml/CustomerMakeOrdersPageView.fxml"));
            Parent root = loader.load();

            CustomerMakeOrdersPageView viewController = loader.getController();
            viewController.setController(controller);
            viewController.setSkateboardBean(skateboardBean);
            viewController.initData();

            Scene scene = new Scene(root, 1200, 800);
            stage.setResizable(false);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void loadDoneOrderPage(CustomOrderController controller, BoardBean skateboardBean, DeliveryDestinationBean deliveryDestinationBean) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/viewFxml/CustomerDoneOrdersPageView.fxml"));
            Parent root = loader.load();

            CustomerDoneOrdersPageView viewController = loader.getController();


            Scene scene = new Scene(root, 1200, 800);
            stage.setResizable(false);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void openBro() throws IOException {
        // Se già aperto, non fare nulla
        if (stageBr != null && stageBr.isShowing()) {
            stageBr.toFront();
            return;
        }

        stageBr = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/viewFxml/CoordinatorOrdersPageView.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root, 1200, 800);


        stageBr.setResizable(false);
        stageBr.setScene(scene);
        stageBr.show();



        Platform.runLater(() -> {
            // Minimizza la finestra principale per un istante
            stage.setIconified(true);

            // Aspetta un attimo prima di ripristinarla
            PauseTransition delay = new PauseTransition(Duration.millis(1));
            delay.setOnFinished(e -> {
                stage.setIconified(false);  // Ripristina la finestra principale
                stage.toFront();  // Porta la finestra principale davanti
                stage.requestFocus();  // Forza il focus sulla finestra principale
            });
            delay.play();
        });
    }

    public void closeBro() {
        if (stageBr != null) {
            stageBr.close();
            stageBr = null; // resettiamo il riferimento
        }
    }

}

