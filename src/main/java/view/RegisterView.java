package view;

import beans.RegisterUserBean;
import controls.LoginController;
import exceptions.EmptyFieldException;
import exceptions.NoUserTypeSelectedException;
import exceptions.PasswordConfirmationException;
import exceptions.UserAlreadyExistsException;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import login.AccountType;
import utils.SceneManager;

import java.io.IOException;

public class RegisterView {
    private final SceneManager sceneManager = SceneManager.getInstance();
    private final LoginController loginController = new LoginController();

    @FXML private TextField nameField;
    @FXML private TextField surnameField;
    @FXML private TextField usernameField;
    @FXML private TextField passwordField;
    @FXML private TextField passwordConfirmationField;
    @FXML private Label errorLabel;
    @FXML private ToggleButton btnCostumer;
    @FXML private ToggleButton btnOrganizer;


    public void validateRegistration(String name, String surname, String username, String password, String passwordConfirmation, ToggleButton selected) throws EmptyFieldException, PasswordConfirmationException, NoUserTypeSelectedException{
        if(name.isEmpty() || surname.isEmpty() ||username.isEmpty() || password.isEmpty()){
            throw new EmptyFieldException("Tutti i campi devono essere compilati!!");
        }

        if(!password.equals(passwordConfirmation)){
            throw new PasswordConfirmationException("Le password non corrispndono");
        }

        if(selected == null){
            throw new NoUserTypeSelectedException("Bisogna Selezionare un tipo di utente");
        }
    }


    @FXML
    public void submitRegistration(){
        String username = usernameField.getText();
        String password = passwordField.getText();
        String passwordConfirmation = passwordConfirmationField.getText();
        String name = nameField.getText();
        String surname = surnameField.getText();

        ToggleButton selected = (ToggleButton) btnCostumer.getToggleGroup().getSelectedToggle();

        try{
            validateRegistration(name,surname, username,password,passwordConfirmation, selected);
            AccountType accountType = (selected == btnCostumer) ? AccountType.COSTUMER : AccountType.ORGANIZER;
            loginController.registerUser(new RegisterUserBean(name,username, username, password,accountType));
            try{
                if (accountType == AccountType.COSTUMER){
                    SceneManager.getInstance().loadScene("CostumerHomePageView.fxml");
                }else{
                    SceneManager.getInstance().loadScene("OrganizerHomePageView.fxml");
                }
            }catch (IOException e){
                System.err.println("Errore di I/O: " + e.getMessage());
                errorLabel.setText("Errore di sistema. Riprova più tardi.");
            }

        }catch (EmptyFieldException | PasswordConfirmationException | NoUserTypeSelectedException | UserAlreadyExistsException e){
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
    public void handleBtnOrganizer(){
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
        }catch(IOException e){
            System.err.println("Errore di I/O: " + e.getMessage());
            errorLabel.setText("Errore di sistema. Riprova più tardi.");
        }
    }

    @FXML
    public void goLogin(){
        try {
            sceneManager.loadScene("LoginView.fxml");
        }catch(IOException e){
            System.err.println("Errore di I/O: " + e.getMessage());
            errorLabel.setText("Errore di sistema. Riprova più tardi.");
        }
    }
}
