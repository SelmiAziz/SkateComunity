package view;
import javafx.fxml.FXML;
import utils.SceneManager;

import java.io.IOException;

public class AccessView {
    //Un disastro
    SceneManager sceneManager = SceneManager.getInstance();

    @FXML
    public void goToRegistrationPage (){
        try {
            sceneManager.loadScene("viewFxml/RegisterView.fxml");
        } catch (IOException e) {
            e.printStackTrace(); // O mostra un alert all'utente
        }
    }

    @FXML
    public void goLogin()throws IOException {
        
        sceneManager.loadScene("viewFxml/LoginView.fxml");
    }


}
