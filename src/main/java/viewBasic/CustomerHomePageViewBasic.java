package viewBasic;

import controls.LoginController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import utils.SceneManager;

import java.io.IOException;

public class CustomerHomePageViewBasic {

    private Stage stage;

    SceneManager sceneManager = SceneManager.getInstance();


    @FXML
    private Label errorLabel;



    public void initialize() {

    }


    @FXML
    public void goToCompetitionsPage() {
        try {
            sceneManager.loadScene("viewFxmlBasic/CustomerCompetitionsPageViewBasic.fxml");
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