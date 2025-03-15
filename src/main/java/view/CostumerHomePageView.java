package view;

import controls.LoginController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import beans.UserInfo;
import utils.SceneManager;

import java.io.IOException;

public class CostumerHomePageView {
    private Stage stage;
    private final SceneManager sceneManager = SceneManager.getInstance();


    @FXML private Label usernameLabel;
    @FXML private Label coinsLabel;
    @FXML private Label errorLabel;

    public void initialize(){
        updateUserInfo();
    }



    @FXML
    public void goToEvents() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/viewFxml/CostumerEventsPageView.fxml"));
        Parent root = null;
        try {
            root = loader.load();
            CostumerEventsPageView userEventsPageView = loader.getController();
            Scene scene = new Scene(root, 1200, 800);
            stage = sceneManager.getStage();
            stage.setResizable(false);
            stage.setScene(scene);
            stage.show();
            Platform.runLater(userEventsPageView::loadEvents);
        } catch (IOException e) {
            errorLabel.setText(e.getMessage());
        }
    }

    private final LoginController loginController = new LoginController();

    void updateUserInfo(){
        UserInfo userInfo = loginController.getCurrentUserInfo();
        usernameLabel.setText(userInfo.getUsername());
        coinsLabel.setText(String.valueOf(userInfo.getCoins()));
    }


    public void logOut()  {
        try {
            sceneManager.loadScene("viewFxml/AccessView.fxml");
        }catch (IOException e){
            errorLabel.setText(e.getMessage());
        }
    }


}
