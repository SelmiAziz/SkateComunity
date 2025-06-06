package viewbasic;

import beans.AuthBean;
import beans.LogUserBean;
import beans.RegisterUserBean;
import controls.LoginController;
import exceptions.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import utils.WindowManagerBasic;

import java.io.IOException;

public class LogPageViewBasic {
    private final WindowManagerBasic windowManagerBasic = WindowManagerBasic.getInstance();
    private final LoginController loginController = new LoginController();
    private final DateValidatorFormatter dateValidatorFormatter = new DateValidatorFormatter();

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

    private static final String SELECTED_COLOR = "-fx-background-color: #1ABC9C;";
    private static final String UNSELECTED_COLOR = "-fx-background-color: lightgray;";


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
            String dateOfBirth = dateValidatorFormatter.formatValidateDate(month, day, year);
            validateRegistration(username, password, passwordConfirmation, selected);

            processRegistration(username, password, role, dateOfBirth, skaterLevel);

        } catch (EmptyFieldException | PasswordConfirmationException |
                 NoUserTypeSelectedException | WrongFormatException e) {
            resultLabel.setText(e.getMessage());
        }
    }

    private void processRegistration(String username, String password, String role,
                                     String dateOfBirth, String skaterLevel) {
        try {
            RegisterUserBean userBean = new RegisterUserBean(username, password, role, dateOfBirth, skaterLevel);
            loginController.registerUser(userBean);

            clearRegistrationFields();
            resultLabel.setText("Registrazione avvenuta con successo!!");

        } catch (UserNameAlreadyUsedException e) {
            String suggestion = e.getSuggestedUsername();
            resultLabel.setText(e.getMessage() + " Suggerimento: " + suggestion);
            usernameField.setText(suggestion);
        } catch (DataAccessException _) {
            resultLabel.setText("Errore durante la registrazione. Riprova più tardi.");
        }
    }

    private void clearRegistrationFields() {
        usernameField.setText("");
        passwordField.setText("");
        passwordConfirmationField.setText("");
        monthField.setText("");
        dayField.setText("");
        yearField.setText("");
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
            loadHomeScene(authBean.getRole());
        } catch (EmptyFieldException | UserNotFoundException | DataAccessException e) {
            resultLabel.setText(e.getMessage());
        }
    }

    private void loadHomeScene(String role) {
        try {
            if ("Costumer".equals(role)) {
                WindowManagerBasic.getInstance().loadScene("viewFxmlBasic/CustomerCompetitionsPageViewBasic.fxml");
            } else {
                WindowManagerBasic.getInstance().loadScene("viewFxmlBasic/OrganizerCompetitionsPageViewBasic.fxml");
            }
        } catch (IOException _) {
            resultLabel.setText("Errore di sistema. Riprova più tardi.");
        }
    }

    @FXML
    public void handleBtnCostumer() {
        if (btnCostumer.isSelected()) {
            btnCostumer.setStyle(SELECTED_COLOR);
            btnOrganizer.setStyle(UNSELECTED_COLOR);
            skillLevelShow(true);
        } else {
            btnCostumer.setStyle(UNSELECTED_COLOR);
            skillLevelShow(false);
        }
    }

    @FXML
    public void handleBtnOrganizer() {
        if (btnOrganizer.isSelected()) {
            btnOrganizer.setStyle(SELECTED_COLOR);
            btnCostumer.setStyle(UNSELECTED_COLOR);
            skillLevelShow(false);
        } else {
            btnOrganizer.setStyle(UNSELECTED_COLOR);
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
