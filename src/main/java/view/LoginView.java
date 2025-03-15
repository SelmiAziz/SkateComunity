package view;

import beans.LogUserBean;
import controls.LoginController;
import exceptions.EmptyFieldException;
import exceptions.UserNotFoundException;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import login.ProfileType;
import utils.SceneManager;

import java.io.IOException;

public class LoginView {
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
            loginController.logUser(new LogUserBean(username, password));
            try{
                LogUserBean logUserBean = loginController.determineProfile();
                if (logUserBean.getProfileType() == ProfileType.COSTUMER){
                    SceneManager.getInstance().loadScene("viewFxml/CostumerHomePageView.fxml");
                }else {
                    SceneManager.getInstance().loadScene("viewFxml/OrganizerHomePageView.fxml");
                }
            }catch (IOException e){
                System.err.println("Errore di I/O: " + e.getMessage());
                errorLabel.setText("Errore di sistema. Riprova più tardi.");
            }

        } catch (EmptyFieldException  | UserNotFoundException e) {
            errorLabel.setText(e.getMessage());
        }
    }


    @FXML
    public void goBack() {
        try {
            sceneManager.loadScene("viewFxml/AccessView.fxml");
        } catch (IOException e) {
            System.err.println("Errore di I/O: " + e.getMessage());
            errorLabel.setText("Errore di sistema. Riprova più tardi.");
        }
    }

    @FXML
    public void goRegistration() {
        try {
            sceneManager.loadScene("viewFxml/RegisterView.fxml");
        } catch (IOException e) {
            System.err.println("Errore di I/O: " + e.getMessage());
            errorLabel.setText("Errore di sistema. Riprova più tardi.");
        }
    }
}
