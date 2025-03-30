package viewBasic;

import beans.WalletBean;
import beans.CompetitionBean;
import beans.CompetitionRegistrationBean;
import controls.SignCompetitionController;
import exceptions.EmptyFieldException;
import exceptions.InsufficientCoinsException;
import exceptions.NoAvailableSeats;
import exceptions.UserAlreadySignedCompetition;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import utils.SceneManager;

import java.io.IOException;
import java.util.List;

public class CustomerCompetitionsPageViewBasic {
    private final SignCompetitionController signCompetitionController = new SignCompetitionController();
    private final SceneManager sceneManager = SceneManager.getInstance();

    @FXML private TextField locationSearch;
    @FXML private TextField monthField;
    @FXML private TextField dayField;
    @FXML private TextField yearField;
    @FXML private Label coinsLabel;
    @FXML private Label generateCodeLabel;
    @FXML private Label assignedSeatLabel;
    @FXML private TextField competitionNameField;
    @FXML private Label errorLabel;
    @FXML private Label competitionDescriptionLabel;
    @FXML private Label competitionSeatsAvailableLabel;
    @FXML private Label competitionCoinsLabel;
    @FXML private Label competitionNameLabel;
    @FXML private ListView<String> competitionList;
    @FXML private Button confirmRegistrationButton;

    public void initialize() {
        List<CompetitionBean> availableCompetitionsBean =  signCompetitionController.allAvailableCompetitions();
        loadCompetitions(availableCompetitionsBean);
        updateWalletInfo();
        confirmRegistrationButton.setVisible(false);
    }

    String formatLocationToView(String location){
        if(location == null) return "";
        return location.replace("-", "/");
    }

    String formatDateToView(String date){
        String[] datArr = date.split("-");
        return datArr[1]+"-"+datArr[0]+"-"+datArr[2];
    }

    public void updateWalletInfo(){
        WalletBean walletBean = signCompetitionController.customerInfo();
        coinsLabel.setText(""+walletBean.getBalance());
    }

    public void competitionDetails(){
        try {
            String competitionName = competitionNameField.getText();
            if(competitionName.isEmpty()){throw new EmptyFieldException("Campo nome competizione risulta vuoto!!");}
            CompetitionBean competitionBean = new CompetitionBean(competitionName);
            CompetitionBean competitionBeanDetailed = signCompetitionController.competitionDetails(competitionBean);
            competitionNameLabel.setText(competitionBeanDetailed.getName());
            competitionCoinsLabel.setText(""+competitionBeanDetailed.getCoins());
            competitionDescriptionLabel.setText(competitionBeanDetailed.getDescription());
            competitionSeatsAvailableLabel.setText(""+competitionBeanDetailed.getAvailableRegistrations());
            confirmRegistrationButton.setVisible(true);
        } catch(EmptyFieldException e){
            errorLabel.setText(e.getMessage());
        }
    }

    public void loadCompetitions(List<CompetitionBean> toLoadCompetitions) {
        competitionList.getItems().clear();
        for (CompetitionBean competitionBean : toLoadCompetitions) {
            String competitionDisplay = String.format(
                    "<<Name Competition: %s>>-<<Date: %s>>-<<Location: %s>>",
                    competitionBean.getName(),
                    formatDateToView(competitionBean.getDate()),
                    formatLocationToView(competitionBean.getLocation())
            );
            competitionList.getItems().add(competitionDisplay);
        }
    }

    String formatDate(String month, String day, String year){
        return day+"-"+month+"-20"+year;
    }

    String formatLocation(String location){ return location.replace("/","-"); }

    @FXML
    public void searchCompetitions() {
        String date = formatDate(monthField.getText(), dayField.getText(), yearField.getText());
        String location = formatLocation(locationSearch.getText());
        CompetitionBean competitionBean = new CompetitionBean(date, location);
        List<CompetitionBean> filteredCompetitions = signCompetitionController.searchCompetitionByDateAndLocation(competitionBean);
        loadCompetitions(filteredCompetitions);
    }

    @FXML
    public void submitSignToCompetition(){
        try {
            String competitionName = competitionNameField.getText();
            if(competitionName.isEmpty()) throw  new EmptyFieldException("Campo nome competizione risulta vuoto!!!");
            CompetitionBean competitionBean = new CompetitionBean(competitionName);
            CompetitionRegistrationBean competitionRegistrationBean = signCompetitionController.signToCompetition(competitionBean);
            assignedSeatLabel.setText("Ti è stato fornito il codice: " +competitionRegistrationBean.getRegistrationCode());
            generateCodeLabel.setText("Turno di gara: "+competitionRegistrationBean.getAssignedSeat());
        } catch (UserAlreadySignedCompetition | InsufficientCoinsException | NoAvailableSeats e) {
            errorLabel.setText(e.getMessage());
        } catch(EmptyFieldException e){
            errorLabel.setText(e.getMessage());
        }
    }

    @FXML
    public void goToCommissionsPage(){

    }

    public void goToHomePage() {
        try {
            sceneManager.loadScene("viewFxmlBasic/CustomerHomePageViewBasic.fxml");
        } catch(IOException e){
            errorLabel.setText(e.getMessage());
        }
    }

    @FXML
    public void goToTricksPage(){
        try {
            sceneManager.loadScene("viewFxmlBasic/CustomerTricksPageViewBasic.fxml");
        } catch(IOException e){
            errorLabel.setText(e.getMessage());
        }
    }

    @FXML
    public void goToCompetitionsPage(){
    }

    public void logOut(){
        try {
            sceneManager.loadScene("viewFxmlBasic/AccessViewBasic.fxml");
        } catch(IOException e){
            errorLabel.setText(e.getMessage());
        }
    }
}

