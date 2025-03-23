package viewBasic;

import beans.LogUserBean;
import controls.LoginController;
import exceptions.EmptyFieldException;
import exceptions.UserNotFoundException;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import login.Role;
import utils.SceneManager;

import java.io.IOException;

public class LoginViewBasic {
    private final SceneManager sceneManager = SceneManager.getInstance();
    private LoginController loginController = new LoginController();

    @FXML private TextField usernameField;
    @FXML private TextField passwordField;
    @FXML private Label errorLabel;


    public void validateLogin(String username, String password) throws EmptyFieldException {
        if (username.isEmpty() || password.isEmpty()) {
            throw new EmptyFieldException("Username e password devono essere compilati!");
        }

    }

    @FXML
    public void submitLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        try {
            validateLogin(username, password);
            LogUserBean logUserBean = loginController.logUser(new LogUserBean(username, password));
            try{
                if (logUserBean.getRole().equals("Costumer")){
                    SceneManager.getInstance().loadScene("viewFxmlBasic/CustomerHomePageViewBasic.fxml");
                }else {
                    SceneManager.getInstance().loadScene("viewFxmlBasic/OrganizerHomePageViewBasic.fxml");
                }
            }catch (IOException e){
                errorLabel.setText("Errore di sistema. Riprova più tardi.");
            }

        } catch (EmptyFieldException  | UserNotFoundException e) {
            errorLabel.setText(e.getMessage());
        }
    }


    @FXML
    public void goBack() {
        try {
            sceneManager.loadScene("viewFxmlBasic/AccessViewBasic.fxml");
        } catch (IOException e) {
            errorLabel.setText("Errore di sistema. Riprova più tardi.");
        }
    }

    @FXML
    public void goRegistration() {
        try {
            sceneManager.loadScene("viewFxmlBasic/RegisterViewBasic.fxml");
        } catch (IOException e) {
            errorLabel.setText("Errore di sistema. Riprova più tardi.");
        }
    }
}
