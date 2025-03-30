package view;

import beans.WalletBean;
import beans.CompetitionBean;
import beans.CompetitionRegistrationBean;
import controls.SignCompetitionController;
import exceptions.InsufficientCoinsException;
import exceptions.NoAvailableSeats;
import exceptions.UserAlreadySignedCompetition;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import utils.SceneManager;

import java.io.IOException;

public class CustomerCompetitionsPageView {
    private final SignCompetitionController signCompetitionController = new SignCompetitionController();
    private final SceneManager sceneManager = SceneManager.getInstance();

    @FXML private TableView<CompetitionBean> competitionTable;
    @FXML private TableColumn<CompetitionBean, String> colName;
    @FXML private TableColumn<CompetitionBean, String> colDescription;
    @FXML private TableColumn<CompetitionBean, String> colDate;
    @FXML private TextField locationSearch;
    @FXML private DatePicker datePicker;
    @FXML private Label competitionDescriptionLabel;
    @FXML private Label competitionCoinsLabel;
    @FXML private Label competitionNameLabel;
    @FXML private Label competitionSeatsAvailableLabel;
    @FXML private Label errorLabel;
    @FXML private Label registrationCodeLabel;
    @FXML private Label assignedSeatLabel;
    @FXML private Label welcomeLabel;
    @FXML private Label coinsLabel;

    String formatDateToView(String date){
        return date.replace("-", "/");
    }

    public void initialize() {
        colName.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getName()));

        colDescription.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getLocation()));

        colDate.setCellValueFactory(cellData ->
                new SimpleStringProperty(formatDateToView(cellData.getValue().getDate())));

        updateCustomerInfo();
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

    public void reviewCompetition(){

    }

    public String formatFromDataPicker(String date){
        String[] parts = date.split("-");
        return parts[2] + "-" + parts[1] + "-" + parts[0];
    }

    @FXML
    public void searchCompetitions() {
        String date = formatFromDataPicker(datePicker.getValue().toString());
        String location = locationSearch.getText();
        CompetitionBean competitionBean = new CompetitionBean(date, location);
        competitionTable.getItems().clear();
        competitionTable.getItems().addAll(signCompetitionController.searchCompetitionByDateAndLocation(competitionBean));
    }

    @FXML
    public void onCompetitionSelected() {
        registrationCodeLabel.setText("");
        assignedSeatLabel.setText("");
        CompetitionBean selected = competitionTable.getSelectionModel().getSelectedItem();
        CompetitionBean competitionBeanDetails = signCompetitionController.competitionDetails(selected);
        showCompetitionDetails(competitionBeanDetails);
    }

    // Mostra i dettagli della competizione selezionata
    private void showCompetitionDetails(CompetitionBean competitionBean) {
        if (competitionBean != null) {
            competitionNameLabel.setText(competitionBean.getName());
            competitionDescriptionLabel.setText(competitionBean.getDescription());
            competitionCoinsLabel.setText(""+ competitionBean.getCoins());
            competitionSeatsAvailableLabel.setText(""+competitionBean.getAvailableRegistrations());
        }
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
            System.err.println(e.getMessage());
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
