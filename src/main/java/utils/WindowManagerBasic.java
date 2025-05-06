package utils;

import beans.AuthBean;
import beans.BoardBean;
import beans.OrderSummaryBean;
import controls.CustomOrderController;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;
import view.*;
import viewBasic.CustomerOrdersPageViewBasic;

import java.io.IOException;

public class WindowManagerBasic{
    private Stage stage;
    private Stage stageBr;
    private static WindowManagerBasic instance;
    private AuthBean authBean;

    private WindowManagerBasic() {} ;

    public synchronized static WindowManagerBasic getInstance() {
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

    public void loadMakeOrdersPage( ) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/viewFxmlBasic/CustomerOrdersPageView.fxml"));
            Parent root = loader.load();

            CustomerOrdersPageViewBasic viewController = loader.getController();
            viewController.setController(controller);
            openCoordinator(controller);

            Scene scene = new Scene(root, 1200, 800);
            stage.setResizable(false);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void goToOrdersPage() throws IOException {
        loadScene("viewFxmlBasic/CustomerOrdersPageViewBasic.fxml");
    }

    public void loadAllOrdersPage(CustomOrderController controller, OrderSummaryBean customOrderBean) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/viewFxml/CustomerAllOrdersPageView.fxml"));
            Parent root = loader.load();

            CustomerAllOrdersPageView viewController = loader.getController();
            viewController.setCustomOrderBean(customOrderBean);
            viewController.setCustomOrderController(controller);
            controller.setCustomAllOrdersPageView(viewController);

            Scene scene = new Scene(root, 1200, 800);
            stage.setResizable(false);
            stage.setScene(scene);
            stage.show();
            viewController.initAfter();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    public void openCoordinator(CustomOrderController customOrderController) throws IOException {
        if (stageBr != null && stageBr.isShowing()) {
            stageBr.toFront();
            return;
        }

        stageBr = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/viewFxml/CoordinatorOrdersPageView.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root, 1200, 800);
        CoordinatorOrderPageView coordinatorController = loader.getController();
        customOrderController.setCoordinatorOrderPageView(coordinatorController);
        coordinatorController.setCustomOrderController(customOrderController);
        coordinatorController.loadOrders();

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

    public void logOut() throws IOException {
        loadScene("viewFxmlBasic/LogPageBasicViewBasic.fxml");

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
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
        Platform.runLater(organizerEventsPageView::loadCompetitions);

    }

    public void goToSkateboards() throws IOException {
        loadScene("viewFxmlBasic/OrganizerSkateboardsPageViewBasic.fxml");
    }


    public void goToLearn() throws IOException {
        loadScene("viewFxmlBasic/CustomerTricksPageViewBasic.fxml");
    }

    public void goToTricks() throws IOException {
        loadScene("viewFxmlBasic/OrganizerTricksPageViewBasic.fxml");
    }


    public void closeCoordinator() {
        if (stageBr != null) {
            stageBr.close();
            stageBr = null;
        }
    }



}
