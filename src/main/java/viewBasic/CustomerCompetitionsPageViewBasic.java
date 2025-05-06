package viewBasic;

import beans.RegistrationBean;
import beans.RegistrationRequestBean;
import beans.WalletBean;
import beans.CompetitionBean;
import controls.SignCompetitionController;
import exceptions.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import utils.WindowManager;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class CustomerCompetitionsPageViewBasic {
    private final SignCompetitionController signCompetitionController = new SignCompetitionController();
    private final WindowManager sceneManager = WindowManager.getInstance();

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
    @FXML private ChoiceBox<String> choicePage;

    public void initialize() {
        List<CompetitionBean> availableCompetitionsBean =  signCompetitionController.allAvailableCompetitions("");
        loadCompetitions(availableCompetitionsBean);
        updateWalletInfo();
        confirmRegistrationButton.setVisible(false);
        populatePageChoice();
    }


    private void populatePageChoice() {
        List<String> list = Arrays.asList( "Commissions", "Learn", "Log Out");
        ObservableList<String> categories = FXCollections.observableArrayList(list);
        choicePage.setItems(categories);
        choicePage.setValue("Competitions");
    }


    public void updateWalletInfo(){
        WalletBean walletBean = signCompetitionController.customerInfo("");
        coinsLabel.setText(""+walletBean.getBalance());
    }

    public void competitionDetails(){
        try {
            String competitionName = competitionNameField.getText();
            if(competitionName.isEmpty()){throw new EmptyFieldException("Campo nome competizione risulta vuoto!!");}
            CompetitionBean competitionBean = new CompetitionBean(competitionName);
            CompetitionBean competitionBeanDetailed = signCompetitionController.competitionDetails("",competitionBean);
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
                    competitionBean.getDate(),
                    competitionBean.getLocation()
            );
            competitionList.getItems().add(competitionDisplay);
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
    public void searchCompetitions() {
        try {
            String date = formatValidateDate(monthField.getText(), dayField.getText(), yearField.getText());
            String location = locationSearch.getText();
            CompetitionBean competitionBean = new CompetitionBean(date, location);
            List<CompetitionBean> filteredCompetitions = signCompetitionController.searchCompetitionByDateAndLocation("",competitionBean);
            loadCompetitions(filteredCompetitions);
        }catch(WrongFormatException e){
            errorLabel.setText(e.getMessage());
        }
    }

    @FXML
    public void submitSignToCompetition(){
        try {
            String competitionName = competitionNameField.getText();
            if(competitionName.isEmpty()) throw  new EmptyFieldException("Campo nome competizione risulta vuoto!!!");
            CompetitionBean competitionBean = new CompetitionBean(competitionName);
            RegistrationBean registrationBean = signCompetitionController.signToCompetition("",competitionBean, new RegistrationRequestBean());
            assignedSeatLabel.setText("Ti è stato fornito il codice: " +registrationBean.getRegistrationCode());
            generateCodeLabel.setText("Turno di gara: "+registrationBean.getAssignedSeat());
        } catch (UserAlreadySignedCompetition | InsufficientCoinsException | NoAvailableSeats e) {
            errorLabel.setText(e.getMessage());
        } catch(EmptyFieldException e){
            errorLabel.setText(e.getMessage());
        }
    }

    @FXML
    public void changePage(){
        String page = choicePage.getValue();
        if(page.equals("Learn")){
            try {
                sceneManager.loadScene("viewFxmlBasic/CustomerTricksPageViewBasic.fxml");
            } catch(IOException e){
                errorLabel.setText(e.getMessage());
            }
        }else if(page.equals("Commissions")){
            try {
                sceneManager.loadScene("viewFxmlBasic/LogPageBasicViewBasic.fxml");
            } catch(IOException e){
                errorLabel.setText(e.getMessage());
            }
        }else if(page.equals("Log Out")){
            try {
                sceneManager.loadScene("viewFxmlBasic/LogPageBasicViewBasic.fxml");
            } catch(IOException e){
                errorLabel.setText(e.getMessage());
            }
        }
    }

    public void logOut(){
        try {
            sceneManager.loadScene("viewFxmlBasic/LogPageBasicViewBasic.fxml");
        } catch(IOException e){
            errorLabel.setText(e.getMessage());
        }
    }
}

