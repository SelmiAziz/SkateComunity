package view;

import beans.RegisterUserBean;
import controls.LoginController;
import exceptions.*;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import login.ProfileType;
import utils.SceneManager;

import java.io.IOException;

public class RegisterView {
    private final SceneManager sceneManager = SceneManager.getInstance();
    private final LoginController loginController = new LoginController();

    @FXML private TextField usernameField;
    @FXML private TextField passwordField;
    @FXML private TextField profileNameField;
    @FXML private TextField passwordConfirmationField;
    @FXML private Label resultLabel;
    @FXML private ToggleButton btnCostumer;
    @FXML private ToggleButton btnOrganizer;


    public void validateRegistration( String username, String password, String passwordConfirmation, ToggleButton selected) throws EmptyFieldException, PasswordConfirmationException, NoUserTypeSelectedException{
        if(username.isEmpty() || password.isEmpty()){
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
        String profileName = profileNameField.getText();
        String passwordConfirmation = passwordConfirmationField.getText();


        ToggleButton selected = (ToggleButton) btnCostumer.getToggleGroup().getSelectedToggle();

        try{
            validateRegistration( username,password,passwordConfirmation, selected);
            ProfileType profileType = (selected == btnCostumer) ? ProfileType.COSTUMER : ProfileType.ORGANIZER;
            loginController.registerUser(new RegisterUserBean(username,password, profileName, profileType));
            usernameField.setText("");
            passwordField.setText("");
            passwordConfirmationField.setText("");
            profileNameField.setText("");
            resultLabel.setText("Registrazione avvenuta con successo!!");
            //try{
                //if (profileType == ProfileType.COSTUMER){
                    //SceneManager.getInstance().loadScene("CostumerHomePageView.fxml");
                //}else{
                    //SceneManager.getInstance().loadScene("OrganizerHomePageView.fxml");
               // }
            //}catch (IOException e){
               // System.err.println("Errore di I/O: " + e.getMessage());
                //errorLabel.setText("Errore di sistema. Riprova più tardi.");
            //}


        }catch (ProfileNameAlreadyUsedException | EmptyFieldException | PasswordConfirmationException | NoUserTypeSelectedException | UserAlreadyExistsException e){
            resultLabel.setText(e.getMessage());
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
            sceneManager.loadScene("viewFxml/AccessView.fxml");
        }catch(IOException e){
            System.err.println("Errore di I/O: " + e.getMessage());
            resultLabel.setText("Errore di sistema. Riprova più tardi.");
        }
    }

    @FXML
    public void goLogin(){
        try {
            sceneManager.loadScene("viewFxml/LoginView.fxml");
        }catch(IOException e){
            System.err.println("Errore di I/O: " + e.getMessage());
            resultLabel.setText("Errore di sistema. Riprova più tardi.");
        }
    }
}
