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

    @FXML private TextField monthField;
    @FXML private TextField dayField;
    @FXML private TextField yearField;

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


    public String getSkaterLevel(){
        RadioButton selectedRadio = (RadioButton) skillGroup.getSelectedToggle();
        if (selectedRadio != null) {
            return selectedRadio.getText();
        } else {
            return "";
        }
    }

    public String formatDateOfBirth(String month, String day, String year) throws WrongFormatException {
        int dayInt, monthInt, yearInt;
        try {
            monthInt = Integer.parseInt(month);
            dayInt = Integer.parseInt(day);
            yearInt = Integer.parseInt("20" + year);
        } catch (NumberFormatException e) {
            throw new WrongFormatException("Errore formato: mese, giorno o anno non validi");
        }

        if (monthInt < 1 || monthInt > 12) {
            throw new WrongFormatException("Errore formato: mese non valido");
        }

        int[] daysInMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

        // Controllo anno bisestile
        if ((yearInt % 4 == 0 && yearInt % 100 != 0) || (yearInt % 400 == 0)) {
            daysInMonth[1] = 29;
        }

        if (dayInt < 1 || dayInt > daysInMonth[monthInt - 1]) {
            throw new WrongFormatException("Errore formato: giorno non valido per il mese");
        }

        String formattedMonth = monthInt < 10 ? "0" + monthInt : "" + monthInt;
        return String.format("%02d/%s/%04d", dayInt, formattedMonth, yearInt);
    }

    @FXML
    public void submitRegistration(){
        try {
            String username = usernameField.getText();
            String password = passwordField.getText();
            String month = monthField.getText();
            String day = dayField.getText();
            String year = yearField.getText();
            String dateOfBirth = formatDateOfBirth(month, day, year);
            String passwordConfirmation = passwordConfirmationField.getText();
            String skaterLevel = getSkaterLevel();

            ToggleButton selected = (ToggleButton) btnCostumer.getToggleGroup().getSelectedToggle();
            validateRegistration(username, password, passwordConfirmation, selected);

            try {
                String role = (selected == btnCostumer) ? "Costumer" : "Organizer";
                loginController.registerUser(new RegisterUserBean(username, password, role, dateOfBirth, skaterLevel));
                usernameField.setText("");
                passwordField.setText("");
                passwordConfirmationField.setText("");
                monthField.setText("");
                dayField.setText("");
                yearField.setText("");
                resultLabel.setText("Registrazione avvenuta con successo!!");


            } catch (UserNameAlreadyUsedException | UserAlreadyExistsException e) {
                resultLabel.setText(e.getMessage());
            }
        }catch(EmptyFieldException | PasswordConfirmationException |
                NoUserTypeSelectedException  | WrongFormatException e){
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
