package view;

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

public class CustomerHomePageView {
    private Stage stage;
    private final SceneManager sceneManager = SceneManager.getInstance();



    @FXML private Label coinsLabel;
    @FXML private Label errorLabel;
    @FXML private Label usernameLabel;

    public void initialize(){

    }

    @FXML
    public void goToCommissionsPage(){}


    @FXML
    public void goToCompetitionsPage() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/viewFxml/CustomerCompetitionsPageView.fxml"));
        Parent root = null;
        try {
            root = loader.load();
            CustomerCompetitionsPageView userEventsPageView = loader.getController();
            Scene scene = new Scene(root, 1200, 800);
            stage = sceneManager.getStage();
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
        System.out.println("Heo");
        try {
            SceneManager.getInstance().loadScene("viewFxml/CustomerTricksPageView.fxml");
        }catch(IOException e){
            errorLabel.setText(e.getMessage());
        }
    }

    private final LoginController loginController = new LoginController();




    public void logOut()  {
        try {
            sceneManager.loadScene("viewFxml/AccessView.fxml");
        }catch (IOException e){
            errorLabel.setText(e.getMessage());
        }
    }


}
