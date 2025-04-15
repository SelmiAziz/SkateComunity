package view;

import javafx.fxml.FXML;
import javafx.scene.layout.Pane;

public class CustomerOrderPageView {

    @FXML private Pane completedCommissionsPane;

    public void initialize(){
        completedCommissionsPane.setVisible(false);
    }

    public void completedCommissions(){
        completedCommissionsPane.setVisible(true);
    }

    public void allCommissions(){
        completedCommissionsPane.setVisible(false);
    }

    public void logOut(){

    }

    public void goToHomePage(){

    }

    public void goToTricksPage(){

    }

    public void goToCompetitionsPage(){

    }
}
