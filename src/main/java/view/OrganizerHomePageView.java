package view;

import beans.UserInfo;
import controls.LoginController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import utils.SceneManager;
import java.io.IOException;

public class OrganizerHomePageView {

    private Stage stage;

    SceneManager sceneManager = SceneManager.getInstance();

    @FXML private Label usernameLabel;
    @FXML private Label coinsLabel;
    @FXML private Label errorLabel;



    public void initialize(){
        updateUserInfo();
    }


    @FXML
    public void goToEvents() {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/viewFxml/OrganizerEventsPageView.fxml"));

        Parent root = null;
        try {
            root = loader.load();
            OrganizerEventsPageView organizerEventsPageView = loader.getController();
            Scene scene = new Scene(root, 1200, 800);
            stage = SceneManager.getInstance().getStage();
            stage.setResizable(false);
            stage.setScene(scene);
            stage.show();
            Platform.runLater(organizerEventsPageView::loadEvents);
        } catch (IOException e) {
            System.err.println("Errore di I/O: " + e.getMessage());
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
        //here you have to call the controller to logOut
        try {
            sceneManager.loadScene("viewFxml/AccessView.fxml");
        }catch(IOException e){
            System.err.println("Errore di I/O: " + e.getMessage());
            errorLabel.setText(e.getMessage());
        }
    }
}
