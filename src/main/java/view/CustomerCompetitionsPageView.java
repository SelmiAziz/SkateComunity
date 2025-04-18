package view;

import beans.WalletBean;
import beans.CompetitionBean;
import beans.CompetitionRegistrationBean;
import controls.SignCompetitionController;
import exceptions.InsufficientCoinsException;
import exceptions.NoAvailableSeats;
import exceptions.UserAlreadySignedCompetition;
import exceptions.WrongFormatException;
import javafx.animation.PauseTransition;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import org.w3c.dom.Text;
import utils.SceneManager;

import java.io.IOException;

public class CustomerCompetitionsPageView {
    private final SignCompetitionController signCompetitionController = new SignCompetitionController();
    private final SceneManager sceneManager = SceneManager.getInstance();

    @FXML private TableView<CompetitionBean> competitionTable;
    @FXML private TableColumn<CompetitionBean, String> colName;
    @FXML private TableColumn<CompetitionBean, String> colLocation;
    @FXML private TableColumn<CompetitionBean, String> colDate;
    @FXML private TextField locationSearch;
    @FXML private TextField dateField;
    @FXML private Label competitionDescriptionLabel;
    @FXML private Label competitionCoinsLabel;
    @FXML private Label competitionNameLabel;
    @FXML private Label competitionSeatsAvailableLabel;
    @FXML private Label errorLabel;
    @FXML private Label registrationCodeLabel;
    @FXML private Label assignedSeatLabel;
    @FXML private Label welcomeLabel;
    @FXML private Label coinsLabel;
    @FXML private Pane hidePane;
    @FXML private Button verifyButton;
    @FXML private Button detailsButton;
    @FXML private Button backButton;
    @FXML private Button selectButton;
    @FXML private Label stateLabel;

    CompetitionBean competitionBean;

    private PauseTransition inactivityTimer;
    private int timeDurationMinutes = 10;


    public void initialize() {
        colName.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getName()));

        colLocation.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getLocation()));

        colDate.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getDate()));

        startConfig();
        updateCustomerInfo();
    }

    public void startConfig(){
        hidePane.setVisible(false);
        verifyButton.setVisible(false);
        detailsButton.setVisible(false);
        backButton.setVisible(false);
        selectButton.setVisible(false);
        competitionCoinsLabel.setText("");
        competitionNameLabel.setText("");
        competitionDescriptionLabel.setText("");
        competitionSeatsAvailableLabel.setText("");
    }

    public void updateCustomerInfo(){
        WalletBean walletBean = signCompetitionController.customerInfo();
        welcomeLabel.setText("Gentile client nel suo saldo sono presenti:");
        coinsLabel.setText(""+walletBean.getBalance());
    }

    public void loadCompetitions() {
        competitionTable.getItems().clear();
        competitionTable.getItems().addAll(signCompetitionController.allAvailableCompetitions());
    }

    public void selectCompetition(){
        showCompetitionInfo();
        competitionInfoConfig();
    }


    public void validaDate(String data) throws WrongFormatException {
        if (data == null || !data.matches("^\\d{2}/\\d{2}/\\d{4}$")) {
            throw new WrongFormatException("Formato non valido (deve essere dd/mm/yyyy)");
        }

        String[] parts = data.split("/");
        int day = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]);
        int year = Integer.parseInt(parts[2]);

        if (month < 1 || month > 12) {
            throw new WrongFormatException("Mese non valido: " + month);
        }

        int[] daysInMonth = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };

        if (month == 2 && ((year % 4 == 0 && year % 100 != 0) || (year % 400 == 0))) {
            daysInMonth[1] = 29;
        }

        if (day < 1 || day > daysInMonth[month - 1]) {
            throw new WrongFormatException("Giorno non valido: " + day + " per il mese " + month);
        }
    }



    @FXML
    public void searchCompetitions() {
        try {
            String date = dateField.getText();
            validaDate(date);
            String location = locationSearch.getText();
            CompetitionBean competitionBean = new CompetitionBean(date, location);
            competitionTable.getItems().clear();
            competitionTable.getItems().addAll(signCompetitionController.searchCompetitionByDateAndLocation(competitionBean));
        }catch(WrongFormatException e) {
            errorLabel.setText(e.getMessage());
        }
    }

    @FXML
    public void onCompetitionSelected() {

        CompetitionBean selected = competitionTable.getSelectionModel().getSelectedItem();
        competitionBean = signCompetitionController.competitionDetails(selected);
        selectButton.setVisible(true);
    }



    private void showCompetitionInfo() {
        if (competitionBean != null) {
            competitionNameLabel.setText("Nome: " +competitionBean.getName());
            competitionDescriptionLabel.setText("Descrizione: " +competitionBean.getDescription());
            stateLabel.setText("SK8: Prendi il tuo skate e competi con tutto il mondo!!!");
        }
        startInactivityTimer();

    }

    public void showCompetitionDetails(){
        if(competitionBean!= null){
            competitionCoinsLabel.setText("Numero di coins richiesto é: "+ competitionBean.getCoins());
            competitionSeatsAvailableLabel.setText("Il numero di posti disponibili al momento è: "+ competitionBean.getAvailableRegistrations());
        }
    }

    public void competitionInfoConfig(){
        hidePane.setVisible(true);
        detailsButton.setVisible(true);
        backButton.setVisible(true);
    }

    public void detailsInfoConf(){
        showCompetitionDetails();
        detailsButton.setVisible(false);
        verifyButton.setVisible(true);
        startInactivityTimer();
    }

    public void backConf(){
        startConfig();
        registrationCodeLabel.setText("");
        assignedSeatLabel.setText("");
        stateLabel.setText("Clicca ad un competitiozione nella tabella accanto ed iscriviti!!");
    }

    private void startInactivityTimer() {
        if (inactivityTimer != null) {
            inactivityTimer.stop();
        }
        inactivityTimer = new PauseTransition(Duration.minutes(timeDurationMinutes));
        inactivityTimer.setOnFinished(event -> handleInactivity());
        inactivityTimer.play();
    }

    private void handleInactivity() {
        backConf();
    }

    @FXML
    public void submitSignToCompetition(){
        try {
            CompetitionBean selectedCompetitionBean = competitionTable.getSelectionModel().getSelectedItem();
            CompetitionRegistrationBean competitionRegistrationBean = signCompetitionController.signToCompetition(selectedCompetitionBean);
            onCompetitionSelected();
            registrationCodeLabel.setText("Il codice della registrazione:" + competitionRegistrationBean.getRegistrationCode());
            assignedSeatLabel.setText("Il posto assegnato è:" + competitionRegistrationBean.getAssignedSeat());
        } catch (UserAlreadySignedCompetition | InsufficientCoinsException | NoAvailableSeats e) {
            errorLabel.setText(e.getMessage());
        }
        updateCustomerInfo();
    }

    @FXML
    public void goToCompetitionsPage(){

    }

    @FXML
    public void goToCommissionsPage(){

    }

    @FXML
    public void goToTricksPage(){
        try {
            SceneManager.getInstance().loadScene("viewFxml/CustomerTricksPageView.fxml");
        }catch(IOException e){
            errorLabel.setText(e.getMessage());
        }
    }

    public void goToHomePage() {
        try {
            sceneManager.loadScene("viewFxml/CustomerHomePageView.fxml");
        }catch(IOException e){
            errorLabel.setText(e.getMessage());
        }
    }

    public void logOut(){
        try {
            sceneManager.loadScene("viewFxml/AccessView.fxml");
        }catch(IOException e){
            errorLabel.setText(e.getMessage());
        }
    }
}
