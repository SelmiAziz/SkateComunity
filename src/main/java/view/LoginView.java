package view;

import beans.AuthBean;
import beans.LogUserBean;
import controls.LoginController;
import exceptions.EmptyFieldException;
import exceptions.UserNotFoundException;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import utils.WindowManager;

import java.io.IOException;

public class LoginView {
    private final WindowManager sceneManager = WindowManager.getInstance();
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
            AuthBean authBean = loginController.logUser(new LogUserBean(username, password));
            WindowManager.getInstance().setAuthBean(authBean);
            loadHomePage(authBean);
        } catch (EmptyFieldException | UserNotFoundException e) {
            errorLabel.setText(e.getMessage());
        }
    }

    private void loadHomePage(AuthBean authBean) {
        try {
            if ("Costumer".equals(authBean.getRole())) {
                WindowManager.getInstance().loadScene("viewFxml/CustomerHomePageView.fxml");
            } else {
                WindowManager.getInstance().loadScene("viewFxml/OrganizerHomePageView.fxml");
            }
        } catch (IOException e) {
            errorLabel.setText(e.getMessage());
        }
    }





    @FXML
    public void goBack() {
        try {
            sceneManager.loadScene("viewFxml/AccessView.fxml");
        } catch (IOException e) {
            errorLabel.setText(e.getMessage());
        }
    }

    @FXML
    public void goRegistration() {
        try {
            sceneManager.loadScene("viewFxml/RegisterView.fxml");
        } catch (IOException e) {
            errorLabel.setText(e.getMessage());
        }
    }
}


