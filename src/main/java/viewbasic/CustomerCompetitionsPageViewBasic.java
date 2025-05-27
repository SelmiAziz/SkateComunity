package viewbasic;

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
import utils.WindowManagerBasic;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class CustomerCompetitionsPageViewBasic {
    private final SignCompetitionController signCompetitionController = new SignCompetitionController();
    private WindowManagerBasic windowManagerBasic = WindowManagerBasic.getInstance();
    private final DateValidatorFormatter dateValidatorFormatter = new DateValidatorFormatter();


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
    @FXML private TextField emailField;
    @FXML private TextField registrationNameField;


    public void initialize() {
            loadAvailableCompetitions();
            updateWalletInfo();
            confirmRegistrationButton.setVisible(false);
            populatePageChoice();
    }


    public void loadAvailableCompetitions(){
        try {
            List<CompetitionBean> availableCompetitionsBean = signCompetitionController.allAvailableCompetitions(windowManagerBasic.getAuthBean().getToken());
            loadCompetitions(availableCompetitionsBean);
        }catch(SessionExpiredException _){
            windowManagerBasic.cleanOrderPage();
            windowManagerBasic.logOut();
        }catch(DataAccessException e){
            errorLabel.setText(e.getMessage());
        }
    }

    private void populatePageChoice() {
        List<String> list = Arrays.asList( "Board", "Learn", "Log Out");
        ObservableList<String> pages = FXCollections.observableArrayList(list);
        choicePage.setItems(pages);
        choicePage.setValue("Competitions");
    }


    public void updateWalletInfo(){
        try {
            WalletBean walletBean = signCompetitionController.customerInfo(windowManagerBasic.getAuthBean().getToken());
            coinsLabel.setText("" + walletBean.getBalance());
        }catch(SessionExpiredException _){
            windowManagerBasic.cleanOrderPage();
            windowManagerBasic.logOut();
        }catch(DataAccessException e){
            errorLabel.setText(e.getMessage());
        }
    }

    public void competitionDetails() {
        try {
            String competitionName = competitionNameField.getText();
            if (competitionName.isEmpty()) {
                throw new EmptyFieldException("Campo nome competizione risulta vuoto!!");
            }
            CompetitionBean competitionBean = new CompetitionBean(competitionName);

            loadCompetitionDetails(competitionBean);

        } catch (EmptyFieldException e) {
            errorLabel.setText(e.getMessage());
        }
    }

    private void loadCompetitionDetails(CompetitionBean competitionBean) {
        try {
            CompetitionBean competitionBeanDetailed = signCompetitionController.competitionDetails(
                    windowManagerBasic.getAuthBean().getToken(), competitionBean);

            competitionNameLabel.setText(competitionBeanDetailed.getName());
            competitionCoinsLabel.setText("" + competitionBeanDetailed.getCoins());
            competitionDescriptionLabel.setText(competitionBeanDetailed.getDescription());
            competitionSeatsAvailableLabel.setText("" + competitionBeanDetailed.getAvailableRegistrations());
            confirmRegistrationButton.setVisible(true);

        } catch (SessionExpiredException _) {
            windowManagerBasic.cleanOrderPage();
            windowManagerBasic.logOut();
        }catch (DataAccessException e){
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



    @FXML
    public void searchCompetitions() {
        try {
            String date = dateValidatorFormatter.formatValidateDate(monthField.getText(), dayField.getText(), yearField.getText());
            String location = locationSearch.getText();
            CompetitionBean competitionBean = new CompetitionBean(date, location);
            searchAndLoadCompetitions(competitionBean);
        } catch (WrongFormatException e) {
            errorLabel.setText(e.getMessage());
        }
    }

    private void searchAndLoadCompetitions(CompetitionBean competitionBean) {
        try {
            List<CompetitionBean> filteredCompetitions = signCompetitionController.searchCompetitionByDateAndLocation(windowManagerBasic.getAuthBean().getToken(), competitionBean);
            loadCompetitions(filteredCompetitions);
        } catch (SessionExpiredException _) {
            windowManagerBasic.cleanOrderPage();
            windowManagerBasic.logOut();
        }catch(DataAccessException e){
            errorLabel.setText(e.getMessage());
        }
    }

    @FXML
    public void submitSignToCompetition() {
        try {
            String competitionName = competitionNameField.getText();
            String email = emailField.getText();
            String registrationName = registrationNameField.getText();

            if (competitionName == null || email == null || registrationName == null) {
                throw new EmptyFieldException("Compila tutti i campi correttamente");
            }

            CompetitionBean competitionBean = new CompetitionBean(competitionName);
            RegistrationRequestBean registrationRequestBean = new RegistrationRequestBean();
            registrationRequestBean.setRegistrationName(registrationName);
            registrationRequestBean.setEmail(email);

            performSignToCompetition(competitionBean, registrationRequestBean);

        } catch (EmptyFieldException | UserAlreadySignedCompetition | InsufficientCoinsException | NoAvailableSeats e) {
            errorLabel.setText(e.getMessage());
        }
    }

    private void performSignToCompetition(CompetitionBean competitionBean, RegistrationRequestBean registrationRequestBean) {
        try {
            RegistrationBean registrationBean = signCompetitionController.signToCompetition(
                    windowManagerBasic.getAuthBean().getToken(), competitionBean, registrationRequestBean);

            assignedSeatLabel.setText("Ti è stato fornito il codice: " + registrationBean.getRegistrationCode());
            generateCodeLabel.setText("Turno di gara: " + registrationBean.getAssignedSeat());

        } catch (SessionExpiredException _) {
            windowManagerBasic.cleanOrderPage();
            windowManagerBasic.logOut();

        } catch (DataAccessException e) {
            errorLabel.setText(e.getMessage());
        }
    }


    @FXML
    public void changePage(){
        String page = choicePage.getValue();
        if(page.equals("Learn")){
            try {
                windowManagerBasic.goToLearn();
            } catch(IOException e){
                errorLabel.setText(e.getMessage());
            }
        }else if(page.equals("Board")){
                windowManagerBasic.goToOrderPage();
        }else if(page.equals("Log Out")){
                windowManagerBasic.logOut();

        }
    }


}

