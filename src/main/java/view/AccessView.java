package view;
import javafx.fxml.FXML;
import utils.WindowManager;

import java.io.IOException;

public class AccessView {
    //Un disastro
    WindowManager sceneManager = WindowManager.getInstance();

    @FXML
    public void goToRegistrationPage (){
        try {
            sceneManager.loadScene("viewFxml/RegisterView.fxml");
        } catch (IOException e) {
            //
        }
    }

    @FXML
    public void goLogin()throws IOException {
        
        sceneManager.loadScene("viewFxml/LoginView.fxml");
    }


}
