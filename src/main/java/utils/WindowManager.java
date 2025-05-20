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

import java.io.IOException;

public class WindowManager {
    private Stage stage;
    private Stage stageBr;
    private static WindowManager instance;
    private AuthBean authBean;

    private WindowManager() {}

    public  static synchronized  WindowManager getInstance() {
        if (instance == null) {
            instance = new WindowManager();
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


    public void removeAuthBean(){
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

        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public void loadMakeOrdersPage( CustomOrderController controller, BoardBean boardBean) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/viewFxml/CustomerMakeOrdersPageView.fxml"));
            Parent root = loader.load();

            CustomerMakeOrdersPageView viewController = loader.getController();
            viewController.setController(controller);
            viewController.setBoardBean(boardBean);
            openCoordinator(controller);

            Scene scene = new Scene(root, 1200, 800);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void goToOrdersPage() throws IOException {
        loadScene("viewFxml/CustomerOrdersPageView.fxml");
    }

    public void loadAllOrdersPage(CustomOrderController controller, OrderSummaryBean customOrderBean) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/viewFxml/CustomerAllOrdersPageView.fxml"));
            Parent root = loader.load();

            CustomerAllOrdersPageView viewController = loader.getController();
            viewController.setCustomOrderBean(customOrderBean);
            viewController.setCustomOrderController(controller);
            controller.setCustomerOrderView(viewController);

            Scene scene = new Scene(root, 1200, 800);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
            viewController.initAfter();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadPreviousOrdersPage(CustomOrderController controller){

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/viewFxml/CustomerAllOrdersPageView.fxml"));
            Parent root = loader.load();

            CustomerAllOrdersPageView viewController = loader.getController();
            viewController.setCustomOrderController(controller);
            controller.setCustomerOrderView(viewController);

            Scene scene = new Scene(root, 1200, 800);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
            viewController.initAfter2();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void goToCustomerCompetitions() throws IOException {
        loadScene("viewFxml/CustomerCompetitionsPageView.fxml");
    }

    public void goToOrganizerCompetitions() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/viewFxml/OrganizerCompetitionsPageView.fxml"));
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
        loadScene("viewFxml/OrganizerSkateboardsPageView.fxml");
    }


    public void goToLearn() throws IOException {
        loadScene("viewFxml/CustomerTricksPageView.fxml");
    }

    public void goToTricks() throws IOException {
        loadScene("viewFxml/OrganizerTricksPageView.fxml");
    }


    public void closeCoordinator() {
        if (stageBr != null) {
            stageBr.close();
            stageBr = null;
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
        customOrderController.setCoordinatorOrderView(coordinatorController);
        coordinatorController.setController(customOrderController);
        coordinatorController.loadOrders();

        stageBr.setResizable(false);
        stageBr.setScene(scene);
        stageBr.show();



        Platform.runLater(() -> {

            stage.setIconified(true);

            PauseTransition delay = new PauseTransition(Duration.millis(1));
            delay.setOnFinished(e -> {
                stage.toFront();
                stage.setIconified(false);
                stage.requestFocus();
            });
            delay.play();
        });
    }

    public void logOut()  {
        try {
            loadScene("viewFxml/AccessView.fxml");
        }catch(IOException _){
            //
        }
    }


    public void goToHomePage() throws IOException {
        loadScene("viewFxml/CustomerHomePageView.fxml");
    }

    public void goToOrganizerHomePage() throws IOException {
        loadScene("viewFxml/OrganizerSkateboardsPageView.fxml");
    }



}

