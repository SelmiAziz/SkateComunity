package view;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import utils.WindowManager;
import utils.SessionManager;

import java.io.IOException;

public class OrganizerHomePageView {


    WindowManager windowManager = WindowManager.getInstance();

    @FXML private Label errorLabel;


    public void initialize(){

    }



    @FXML
    public void goToCompetitionsPage() {

    }

    @FXML
    public void goToTricksPage(){
        try {
            windowManager.goToTricks();
        }catch(IOException e){
            errorLabel.setText(e.getMessage());
        }
    }

    @FXML
    public void goToSkateboardsPage(){
        try {
            windowManager.goToSkateboards();
        } catch (IOException e) {
            errorLabel.setText(e.getMessage());
        }
    }






    public void logOut()  {
           windowManager.logOut();

    }


}
