package view;

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

    @FXML private Label errorLabel;


    public void initialize(){

    }



    @FXML
    public void goToCompetitionsPage() {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/viewFxml/OrganizerCompetitionsPageView.fxml"));
        Parent root = null;
        try {
            root = loader.load();
            OrganizerCompetitionsPageView organizerEventsPageView = loader.getController();
            Scene scene = new Scene(root, 1200, 800);
            stage = SceneManager.getInstance().getStage();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
            Platform.runLater(organizerEventsPageView::loadCompetitions);
        } catch (IOException e) {
            errorLabel.setText(e.getMessage());
        }



    }

    @FXML
    public void goToTricksPage(){
        try {
            SceneManager.getInstance().loadScene("viewFxml/OrganizerTricksPageView.fxml");
        }catch(IOException e){
            errorLabel.setText(e.getMessage());
        }
    }

    @FXML
    public void goToCommissionsPage(){

    }



    public void logOut()  {
        //here you have to call the controller to logOut
        try {
            sceneManager.loadScene("viewFxml/AccessView.fxml");
        }catch(IOException e){
            errorLabel.setText(e.getMessage());
        }
    }


}
