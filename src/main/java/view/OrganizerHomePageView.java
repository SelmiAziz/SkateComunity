package view;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import utils.WindowManager;

import java.io.IOException;

public class OrganizerHomePageView {


    WindowManager windowManager = WindowManager.getInstance();

    @FXML private Label errorLabel;




    @FXML
    public void goToCompetitionsPage() {
        try{
            windowManager.goToCustomerCompetitions();
        }catch (IOException e){
            errorLabel.setText(e.getMessage());
        }
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
