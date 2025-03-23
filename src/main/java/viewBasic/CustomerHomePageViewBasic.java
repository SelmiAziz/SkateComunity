package viewBasic;

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
import view.CustomerEventsPageView;

import java.io.IOException;

public class CustomerHomePageViewBasic {

    private Stage stage;

    SceneManager sceneManager = SceneManager.getInstance();

    @FXML
    private Label usernameLabel;
    @FXML
    private Label errorLabel;
    @FXML
    private Label coinsLabel;


    public void initialize() {

    }


    @FXML
    public void goToEvents() {
        System.out.println("Sto andando agli eventi");
        try {
            sceneManager.loadScene("viewFxmlBasic/CustomerEventsPageViewBasic.fxml");
        } catch (IOException e) {
            errorLabel.setText(e.getMessage());
        }

    }


    @FXML
    public void goToTricksPage() {
        try {
            sceneManager.loadScene("viewFxmlBasic/CustomerTricksPageViewBasic.fxml");
        } catch (IOException e) {
            errorLabel.setText(e.getMessage());
        }
    }
    @FXML
    public void goToCompetitionsPage(){

    }

    public void logOut() {
        //here you have to call the controller to logOut
        try {
            sceneManager.loadScene("viewFxmlBasic/AccessViewBasic.fxml");
        } catch (IOException e) {
            errorLabel.setText(e.getMessage());
        }
    }

    private final LoginController loginController = new LoginController();


}