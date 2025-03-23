package viewBasic;

import beans.RegisterUserBean;
import controls.LoginController;
import exceptions.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import utils.SceneManager;

import java.io.IOException;

public class RegisterViewBasic {
    private final SceneManager sceneManager = SceneManager.getInstance();
    private final LoginController loginController = new LoginController();

    @FXML private TextField usernameField;
    @FXML private TextField passwordField;
    @FXML private TextField profileNameField;
    @FXML private TextField passwordConfirmationField;
    @FXML private Label resultLabel;
    @FXML private ToggleButton btnCostumer;
    @FXML private ToggleButton btnOrganizer;
    @FXML private RadioButton noviceRadio;
    @FXML private RadioButton proficientRadio;
    @FXML private RadioButton advancedRadio;
    @FXML private ToggleGroup skillGroup;
    @FXML private Label skillLabel;


    public void initialize(){
        skillGroup = new ToggleGroup();
        noviceRadio.setToggleGroup(skillGroup);
        proficientRadio.setToggleGroup(skillGroup);
        advancedRadio.setToggleGroup(skillGroup);
        skillLevelShow(false);
    }

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

    public void skillLevelShow(boolean f){
        noviceRadio.setVisible(f);
        proficientRadio.setVisible(f);
        advancedRadio.setVisible(f);
        skillLabel.setVisible(f);
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
            String  role = (selected == btnCostumer) ? "Costumer" : "Organizerr";
            loginController.registerUser(new RegisterUserBean(username,password, "", role));
            usernameField.setText("");
            passwordField.setText("");
            passwordConfirmationField.setText("");
            profileNameField.setText("");
            resultLabel.setText("Registrazione avvenuta con successo!!");


        }catch (UserNameAlreadyUsedException | EmptyFieldException | PasswordConfirmationException | NoUserTypeSelectedException | UserAlreadyExistsException e){
            resultLabel.setText(e.getMessage());
        }
    }

    @FXML
    public void handleBtnCostumer() {
        if (btnCostumer.isSelected()) {
            btnCostumer.setStyle("-fx-background-color:  #1ABC9C;");
            btnOrganizer.setStyle("-fx-background-color: lightgray;");
            skillLevelShow(true);
        } else {
            btnCostumer.setStyle("-fx-background-color: lightgray;");
            skillLevelShow(false);
        }
    }

    @FXML
    public void handleBtnOrganizer(){
        if (btnOrganizer.isSelected()) {
            btnOrganizer.setStyle("-fx-background-color:  #1ABC9C;");
            btnCostumer.setStyle("-fx-background-color: lightgray;");
            skillLevelShow(false);
        } else {
            btnOrganizer.setStyle("-fx-background-color: lightgray;");
            skillLevelShow(false);
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
