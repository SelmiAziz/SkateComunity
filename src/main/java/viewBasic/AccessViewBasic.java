package viewBasic;
import javafx.fxml.FXML;
import utils.SceneManager;

import java.io.IOException;

public class AccessViewBasic {
    //Un disastro
    SceneManager sceneManager = SceneManager.getInstance();

    @FXML
    public void goToRegistrationPage (){
        try {
            sceneManager.loadScene("viewFxmlBasic/RegisterViewBasic.fxml");
        } catch (IOException e) {
            e.printStackTrace(); 
        }
    }

    @FXML
    public void goLogin()throws IOException {

        sceneManager.loadScene("viewFxmlBasic/LoginViewBasic.fxml");
    }


}