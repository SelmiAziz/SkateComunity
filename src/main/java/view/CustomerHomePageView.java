package view;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import utils.WindowManager;

import java.io.IOException;

public class CustomerHomePageView {
    private final WindowManager windowManager = WindowManager.getInstance();
    private Stage stage;


    @FXML private Label errorLabel;

    public void initialize(){

    }

    @FXML
    public void goToOrdersPage(){
        try {
            windowManager.goToOrdersPage();
        } catch (IOException e) {
            errorLabel.setText(e.getMessage());
        }
    }


    @FXML
    public void goToCompetitionsPage() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/viewFxml/CustomerCompetitionsPageView.fxml"));
        Parent root = null;
        try {
            root = loader.load();
            CustomerCompetitionsPageView userEventsPageView = loader.getController();
            Scene scene = new Scene(root, 1200, 800);
            stage = windowManager.getStage();
            stage.setResizable(false);
            stage.setScene(scene);
            stage.show();
            Platform.runLater(userEventsPageView::loadCompetitions);
        } catch (IOException e) {
            errorLabel.setText(e.getMessage());
        }
    }



    @FXML
    public void goToTricksPage(){
        try {
           windowManager.goToLearn();
        } catch (IOException e) {
            errorLabel.setText(e.getMessage());
        }
    }


    public void logOut() {
        windowManager.logOut();
    }

}
