package view;

import beans.RegisterUserBean;
import controls.LoginController;
import exceptions.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import utils.WindowManager;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class RegisterView {
    private final WindowManager sceneManager = WindowManager.getInstance();
    private final LoginController loginController = new LoginController();
    private final DateValidator formatView = new DateValidator();

    @FXML private TextField usernameField;
    @FXML private TextField passwordField;
    @FXML private TextField dateBirthField;
    @FXML private ChoiceBox<String> levelChoice;
    @FXML private Label levelLabel;
    @FXML private TextField passwordConfirmationField;
    @FXML private Label resultLabel;
    @FXML private ToggleButton btnCostumer;
    @FXML private ToggleButton btnOrganizer;


    public void initialize(){
        populateSkillLevelChoiceBox();
        levelChoice.setVisible(false);
        levelLabel.setVisible(false);
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



    @FXML
    public void submitRegistration() {
        try {
            String username = usernameField.getText();
            String password = passwordField.getText();
            String dateOfBirth = dateBirthField.getText();
            formatView.validaDate(dateOfBirth);

            String passwordConfirmation = passwordConfirmationField.getText();
            ToggleButton selected = (ToggleButton) btnCostumer.getToggleGroup().getSelectedToggle();

            validateRegistration(username, password, passwordConfirmation, selected);

            String role = (selected == btnCostumer) ? "Costumer" : "Organizer";
            String skillLevel = levelChoice.getValue().toString();

            RegisterUserBean userBean = new RegisterUserBean(username, password, role, dateOfBirth, skillLevel);

            try {
                loginController.registerUser(userBean);
                usernameField.setText("");
                passwordField.setText("");
                passwordConfirmationField.setText("");
                dateBirthField.setText("");
                resultLabel.setText("Registrazione avvenuta con successo!!");
            } catch (UserNameAlreadyUsedException e) {
                resultLabel.setText(e.getMessage() + ". Prova con: " + e.getSuggestedUsername());
                usernameField.setText(e.getSuggestedUsername()); // opzionale: precompila il campo
            }
        } catch (WrongFormatException | EmptyFieldException | PasswordConfirmationException | NoUserTypeSelectedException e) {
            resultLabel.setText(e.getMessage());
        }
    }


    @FXML
    public void handleBtnCostumer() {
        if (btnCostumer.isSelected()) {
            btnCostumer.setStyle("-fx-background-color:  #1ABC9C;");
            btnOrganizer.setStyle("-fx-background-color: lightgray;");
            levelChoice.setVisible(true);
            levelLabel.setVisible(true);
        } else {
            btnCostumer.setStyle("-fx-background-color: lightgray;");
            levelChoice.setVisible(false);
            levelLabel.setVisible(false);
        }
    }

    @FXML
    public void handleBtnOrganizer(){
        if (btnOrganizer.isSelected()) {
            btnOrganizer.setStyle("-fx-background-color:  #1ABC9C;");
            btnCostumer.setStyle("-fx-background-color: lightgray;");
            levelChoice.setVisible(false);
            levelLabel.setVisible(false);
        } else {
            btnOrganizer.setStyle("-fx-background-color: lightgray;");
            levelChoice.setVisible(false);
            levelLabel.setVisible(false);
        }
    }

    private void populateSkillLevelChoiceBox() {
        List<String> skillList = Arrays.asList("Novice", "Proficient" , "Advanced");
        ObservableList<String> skills = FXCollections.observableArrayList(skillList);
        levelChoice.setItems(skills);
        levelChoice.setValue("Proficient");
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
