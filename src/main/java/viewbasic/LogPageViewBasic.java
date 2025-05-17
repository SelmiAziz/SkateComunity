package viewBasic;

import beans.AuthBean;
import beans.LogUserBean;
import beans.RegisterUserBean;
import controls.LoginController;
import exceptions.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import utils.WindowManager;
import utils.WindowManagerBasic;

import java.io.IOException;

public class LogPageViewBasic {
    private final WindowManagerBasic windowManagerBasic = WindowManagerBasic.getInstance();
    private final LoginController loginController = new LoginController();

    @FXML private TextField usernameField;
    @FXML private TextField passwordField;

    @FXML private TextField monthField;
    @FXML private TextField dayField;
    @FXML private TextField yearField;
    @FXML private Pane loginPane;
    @FXML private Button goRegister;
    @FXML private Button goLogin;
    @FXML private TextField passwordConfirmationField;
    @FXML private Label resultLabel;
    @FXML private ToggleButton btnCostumer;
    @FXML private ToggleButton btnOrganizer;
    @FXML private RadioButton noviceRadio;
    @FXML private RadioButton proficientRadio;
    @FXML private RadioButton advancedRadio;
    @FXML private ToggleGroup skillGroup;
    @FXML private Label skillLabel;
    @FXML private TextField usernameFieldLog;
    @FXML private TextField passwordFieldLog;

    public void initialize(){
        skillGroup = new ToggleGroup();
        noviceRadio.setToggleGroup(skillGroup);
        proficientRadio.setToggleGroup(skillGroup);
        advancedRadio.setToggleGroup(skillGroup);
        skillLevelShow(false);
        goLogin.setVisible(false);
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
    private String formatValidateDate(String month, String day, String year) throws WrongFormatException {
        int monthInt = Integer.parseInt(month);
        if (monthInt < 1 || monthInt > 12) {
            throw new WrongFormatException("Mese non valido: " + month);
        }

        int dayInt = Integer.parseInt(day);
        int[] daysInMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

        int yearInt = Integer.parseInt(year);
        if (monthInt == 2 && ((yearInt % 4 == 0 && yearInt % 100 != 0) || (yearInt % 400 == 0))) {
            daysInMonth[1] = 29;
        }

        if (dayInt < 1 || dayInt > daysInMonth[monthInt - 1]) {
            throw new WrongFormatException("Giorno non valido: " + day + " per il mese " + month);
        }

        return day + "/" + month + "/20" + year;
    }

    @FXML
    public void doRegistration(){
        try {
            String username = usernameField.getText();
            String password = passwordField.getText();
            String month = monthField.getText();
            String day = dayField.getText();
            String year = yearField.getText();
            String passwordConfirmation = passwordConfirmationField.getText();
            String skaterLevel = getSkaterLevel();

            ToggleButton selected = (ToggleButton) btnCostumer.getToggleGroup().getSelectedToggle();
            String role = (selected == btnCostumer) ? "Costumer" : "Organizer";
            String dateOfBirth = formatValidateDate(month, day, year);
            validateRegistration(username, password, passwordConfirmation, selected);

            try {
                RegisterUserBean userBean = new RegisterUserBean(username, password, role, dateOfBirth, skaterLevel);
                loginController.registerUser(userBean);

                // Pulizia campi
                usernameField.setText("");
                passwordField.setText("");
                passwordConfirmationField.setText("");
                monthField.setText("");
                dayField.setText("");
                yearField.setText("");

                resultLabel.setText("Registrazione avvenuta con successo!!");

            } catch (UserNameAlreadyUsedException e) {
                String suggestion = e.getSuggestedUsername();
                resultLabel.setText(e.getMessage() + " Suggerimento: " + suggestion);
                usernameField.setText(suggestion);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } catch(EmptyFieldException | PasswordConfirmationException |
                NoUserTypeSelectedException | WrongFormatException e){
            resultLabel.setText(e.getMessage());
        }
    }


    public void validateLogin(String username, String password) throws EmptyFieldException {
        if (username.isEmpty() || password.isEmpty()) {
            throw new EmptyFieldException("Username e password devono essere compilati!");
        }

    }

    @FXML
    public void doLogin() {
        String username = usernameFieldLog.getText();
        String password = passwordFieldLog.getText();

        try {
            validateLogin(username, password);
            AuthBean authBean = loginController.logUser(new LogUserBean(username, password));
            windowManagerBasic.setAuthBean(authBean);
            try{
                if (authBean.getRole().equals("Costumer")){
                    WindowManagerBasic.getInstance().loadScene("viewFxmlBasic/CustomerCompetitionsPageViewBasic.fxml");
                }else {
                    WindowManagerBasic.getInstance().loadScene("viewFxmlBasic/OrganizerCompetitionsPageViewBasic.fxml");
                }
            }catch (IOException e){
                resultLabel.setText("Errore di sistema. Riprova più tardi.");
            }

        } catch (EmptyFieldException  | UserNotFoundException e) {
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
    public void goLogin(){
        loginPane.setVisible(true);
        goRegister.setVisible(true);
        goLogin.setVisible(false);
    }


    @FXML
    public void goRegister(){
        loginPane.setVisible(false);
        goRegister.setVisible(false);
        goLogin.setVisible(true);
    }
}
