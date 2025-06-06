package utils;

import beans.AuthBean;
import controls.CustomOrderController;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;
import view.*;
import viewbasic.CoordinatorOrderPageViewBasic;
import viewbasic.CustomerOrdersPageViewBasic;

import java.io.IOException;

public class WindowManagerBasic{
    private Stage stage;
    private Stage stageBr;
    private static WindowManagerBasic instance;
    private AuthBean authBean;

    private WindowManagerBasic() {}

    public static synchronized  WindowManagerBasic getInstance() {
        if (instance == null) {
            instance = new WindowManagerBasic();
        }
        return instance;
    }

    public void setStage(Stage stage) {
        if (this.stage == null) {
            this.stage = stage;
        }
    }


    public void setAuthBean(AuthBean authBean){
        this.authBean = authBean;
    }

    public AuthBean getAuthBean(){
        return authBean;
    }


    public void cleanAuthBean(){
        this.authBean = null;
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

    public void goToOrderPage( ) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/viewFxmlBasic/CustomerOrdersPageViewBasic.fxml"));
            Parent root = loader.load();

            CustomerOrdersPageViewBasic viewController = loader.getController();

            CustomOrderController controller = new CustomOrderController();
            viewController.setController(controller);
            controller.setCustomerOrderView(viewController);
            viewController.initializeAfter();
            openCoordinator(controller);

            Scene scene = new Scene(root, 1200, 800);
            stage.setResizable(false);
            stage.setScene(scene);
            stage.show();

        } catch (IOException _) {
            //
        }
    }

    public void logOut() {
        try {
            loadScene("viewFxmlBasic/LogPageViewBasic.fxml");
        }catch(IOException _){
            //has to be developed
        }

    }


    public void openCoordinator(CustomOrderController customOrderController) throws IOException {
        if (stageBr != null && stageBr.isShowing()) {
            stageBr.toFront();
            return;
        }

        stageBr = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/viewFxmlBasic/CoordinatorOrdersPageViewBasic.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root, 1200, 800);
        CoordinatorOrderPageViewBasic coordinatorController = loader.getController();
        customOrderController.setCoordinatorOrderView(coordinatorController);
        coordinatorController.setController(customOrderController);
        coordinatorController.displayOrders();

        stageBr.setResizable(false);
        stageBr.setScene(scene);
        stageBr.show();



        Platform.runLater(() -> {

            stage.setIconified(true);

            PauseTransition delay = new PauseTransition(Duration.millis(1));
            delay.setOnFinished(e -> {
                stage.setIconified(false);
                stage.toFront();
                stage.requestFocus();
            });
            delay.play();
        });
    }


    public void cleanOrderPage() {
        if (stageBr != null) {
            stageBr.close();
            stageBr = null;
        }
    }


    public void goToCustomerCompetitions() throws IOException {
        loadScene("viewFxmlBasic/CustomerCompetitionsPageViewBasic.fxml");
    }

    public void goToOrganizerCompetitions() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/viewFxmlBasic/OrganizerCompetitionsPageViewBasic.fxml"));
        Parent root = null;
        root = loader.load();
        OrganizerCompetitionsPageView organizerEventsPageView = loader.getController();
        Scene scene = new Scene(root, 1200, 800);
        stage = WindowManager.getInstance().getStage();
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
        Platform.runLater(organizerEventsPageView::loadCompetitions);

    }

    public void goToSkateboards() throws IOException {
        loadScene("viewFxmlBasic/OrganizerSkateboardsPageViewBasic.fxml");
    }

    public void goToTricks() throws IOException {
        loadScene("viewFxmlBasic/OrganizerTricksPageViewBasic.fxml");
    }


    public void goToLearn() throws IOException {
        loadScene("viewFxmlBasic/CustomerTricksPageViewBasic.fxml");
    }




}
