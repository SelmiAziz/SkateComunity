package view;


import utils.SceneManager;

import java.io.IOException;

public class CustomerOrdersPageView {



    public void initialize(){

        System.out.println("Hello");
    }



    public void logOut(){

    }

    public void goToHomePage(){
        SceneManager.getInstance().closeBro();
        try {
            SceneManager.getInstance().loadScene("viewFxml/CustomerHomePageView.fxml");
        } catch (IOException e) {
            System.out.println("HIHI");
        }
    }

    public void goToTricksPage(){

    }

    public void goToCompetitionsPage(){

    }
}
