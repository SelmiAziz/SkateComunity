package view;

import beans.LogUserBean;
import controls.LoginController;
import exceptions.EmptyFieldException;
import exceptions.NoUserTypeSelectedException;
import exceptions.UserNotFoundException;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import login.AccountType;
import utils.SceneManager;

import java.io.IOException;

public class LoginView {
    private final SceneManager sceneManager = SceneManager.getInstance();
    private LoginController loginController = new LoginController();

    @FXML private TextField usernameField;
    @FXML private TextField passwordField;
    @FXML private Label errorLabel;
    @FXML private ToggleButton btnCostumer;
    @FXML private ToggleButton btnOrganizer;

    public void validateLogin(String username, String password, ToggleButton selected) throws EmptyFieldException, NoUserTypeSelectedException {
        if (username.isEmpty() || password.isEmpty()) {
            throw new EmptyFieldException("Username e password devono essere compilati!");
        }
        if (selected == null) {
            throw new NoUserTypeSelectedException("Bisogna selezionare un tipo di utente");
        }
    }

    @FXML
    public void submitLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        ToggleButton selected = (ToggleButton) btnCostumer.getToggleGroup().getSelectedToggle();

        try {
            validateLogin(username, password, selected);
            AccountType accountType = (selected == btnCostumer) ? AccountType.COSTUMER : AccountType.ORGANIZER;
            loginController.logUser(new LogUserBean(username, password, accountType));

            try {
                if (accountType == AccountType.COSTUMER) {
                    sceneManager.loadScene("CostumerHomePageView.fxml");
                } else {
                    sceneManager.loadScene("OrganizerHomePageView.fxml");
                }
            } catch (IOException e) {
                System.err.println("Errore di I/O: " + e.getMessage());
                errorLabel.setText("Errore di sistema. Riprova più tardi.");
            }
        } catch (EmptyFieldException | NoUserTypeSelectedException | UserNotFoundException e) {
            errorLabel.setText(e.getMessage());
        }
    }

    @FXML
    public void handleBtnCostumer() {
        if (btnCostumer.isSelected()) {
            btnCostumer.setStyle("-fx-background-color:  #1ABC9C;");
            btnOrganizer.setStyle("-fx-background-color: lightgray;");
        } else {
            btnCostumer.setStyle("-fx-background-color: lightgray;");
        }
    }

    @FXML
    public void handleBtnOrganizer() {
        if (btnOrganizer.isSelected()) {
            btnOrganizer.setStyle("-fx-background-color:  #1ABC9C;");
            btnCostumer.setStyle("-fx-background-color: lightgray;");
        } else {
            btnOrganizer.setStyle("-fx-background-color: lightgray;");
        }
    }

    @FXML
    public void goBack() {
        try {
            sceneManager.loadScene("AccessView.fxml");
        } catch (IOException e) {
            System.err.println("Errore di I/O: " + e.getMessage());
            errorLabel.setText("Errore di sistema. Riprova più tardi.");
        }
    }

    @FXML
    public void goRegistration() {
        try {
            sceneManager.loadScene("RegisterView.fxml");
        } catch (IOException e) {
            System.err.println("Errore di I/O: " + e.getMessage());
            errorLabel.setText("Errore di sistema. Riprova più tardi.");
        }
    }
}
