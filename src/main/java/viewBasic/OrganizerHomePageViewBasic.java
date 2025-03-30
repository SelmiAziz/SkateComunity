package viewBasic;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import utils.SceneManager;

import java.io.IOException;

public class OrganizerHomePageViewBasic {

    private Stage stage;

    SceneManager sceneManager = SceneManager.getInstance();

    @FXML private Label errorLabel;






    @FXML
    public void goToEvents() {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/viewFxmlBasic/OrganizerCompetitionsPageViewBasic.fxml"));
        Parent root = null;
        try {
            root = loader.load();
            OrganizerCompetitionsPageViewBasic organizerEventsPageViewBasic = loader.getController();
            Scene scene = new Scene(root, 1200, 800);
            stage = SceneManager.getInstance().getStage();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
            Platform.runLater(organizerEventsPageViewBasic::loadCompetitions);
        } catch (IOException e) {
            errorLabel.setText(e.getMessage());
        }

    }

    @FXML
    public void goToTricksPage(){
        try {
            sceneManager.loadScene("viewFxmlBasic/OrganizerTricksPageViewBasic.fxml");
        }catch(IOException e){
            errorLabel.setText(e.getMessage());
        }
    }

    public void goToCompetitionsPage(){

    }


    public void logOut()  {
        //here you have to call the controller to logOut
        try {
            sceneManager.loadScene("viewFxmlBasic/AccessViewBasic.fxml");
        }catch(IOException e){
            errorLabel.setText(e.getMessage());
        }
    }


}
