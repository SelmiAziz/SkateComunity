package view;
import javafx.fxml.FXML;
import utils.SceneManager;

import java.io.IOException;

public class AccessView {
    //Un disastro
    SceneManager sceneManager = SceneManager.getSingletonInstance();

    @FXML
    public void goToRegistrationPage (){
        try {
            SceneManager.getSingletonInstance().loadScene("RegisterView.fxml");
        } catch (IOException e) {
            e.printStackTrace(); // O mostra un alert all'utente
        }
    }

    @FXML
    public void goLogin()throws IOException {
        sceneManager.loadScene("LoginView.fxml");
    }
}
