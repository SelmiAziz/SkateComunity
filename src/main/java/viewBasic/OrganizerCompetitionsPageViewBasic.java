package viewBasic;

import beans.CompetitionBean;
import controls.CreateCompetitionController;
import exceptions.EmptyFieldException;
import exceptions.CompetitionAlreadyExistsException;
import exceptions.SessionExpiredException;
import exceptions.WrongFormatException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import utils.Session;
import utils.WindowManager;
import utils.WindowManagerBasic;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class OrganizerCompetitionsPageViewBasic {
    @FXML private TextField competitionNameField;
    @FXML private TextField monthField;
    @FXML private TextField dayField;
    @FXML private TextField yearField;
    @FXML private TextArea competitionDescriptionArea;
    @FXML private TextField coinsAmountField;
    @FXML private TextField maxRegistrationsField;
    @FXML private ListView<String> competitionList;
    @FXML private TextField locationField;
    @FXML private Label errorLabel;
    @FXML private ChoiceBox<String> choicePage;

    private final CreateCompetitionController createCompetitionController = new CreateCompetitionController();
    private final WindowManagerBasic windowManagerBasic = WindowManagerBasic.getInstance();

    public void initialize(){
        populatePageChoice();
    }

    private void populatePageChoice() {
        List<String> list = Arrays.asList( "Skateboards", "Tricks", "Log Out");
        ObservableList<String> pages = FXCollections.observableArrayList(list);
        choicePage.setItems(pages);
        choicePage.setValue("Competitions");
    }




    public void loadCompetitions(){
        try {
            List<CompetitionBean> availableCompetitionsBean = createCompetitionController.organizerCompetitions(windowManagerBasic.getAuthBean().getToken());
            competitionList.getItems().clear();
            for (CompetitionBean competitionBean : availableCompetitionsBean) {
                String competitionDisplay = String.format(
                        "<<Name Competition: %s>>-<<Description: %s>>-<<Date: %s>>-<<Location: %s>>-<<Coins Required: %d>>-<<CurrentRegistration: %d>>-<<MaxRegistration: %d>>",
                        competitionBean.getName(),
                        competitionBean.getDescription(),
                        competitionBean.getDate(),
                        competitionBean.getLocation(),
                        competitionBean.getCoins(),
                        competitionBean.getCurrentRegistrations(),
                        competitionBean.getMaxRegistrations()
                );
                competitionList.getItems().add(competitionDisplay);
            }
        }catch(SessionExpiredException e ){
            windowManagerBasic.logOut();
        }
    }

    private void validateFields(String name, String description, String date, String location) throws EmptyFieldException{
        if (name.isEmpty() || description.isEmpty() || date.isEmpty() || location.isEmpty()) {
            throw new EmptyFieldException("I campi per la creazione della competizione devono essere tutti compilati!!!");
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
    private void createCompetition() {
        String name = competitionNameField.getText();
        String description = competitionDescriptionArea.getText();
        String month = monthField.getText();
        String day = dayField.getText();
        String year = yearField.getText();
        String location = locationField.getText();
        int coinsRequired, maxRegistrations;

        try{
            String date = formatValidateDate(month, day, year);
            validateFields(name, description, date, location);
            coinsRequired = Integer.parseInt(coinsAmountField.getText());
            maxRegistrations = Integer.parseInt(maxRegistrationsField.getText());
            try {
                createCompetitionController.createCompetition(windowManagerBasic.getAuthBean().getToken(), new CompetitionBean(name, description, date, location, coinsRequired, maxRegistrations));
                loadCompetitions();
            }catch(SessionExpiredException e){
                windowManagerBasic.logOut();
            }
        } catch (NumberFormatException | EmptyFieldException | WrongFormatException |
                 CompetitionAlreadyExistsException | SQLException e) {
            errorLabel.setText(e.getMessage());
        }
    }

    @FXML
    public void changePage(){
        String page = choicePage.getValue();
        if(page.equals("Tricks")){
            try {
                windowManagerBasic.goToTricks();
            } catch(IOException e){
                errorLabel.setText(e.getMessage());
            }
        }else if(page.equals("Skateboards")){
            try {
                windowManagerBasic.goToSkateboards();
            } catch(IOException e){
                errorLabel.setText(e.getMessage());
            }
        }else if(page.equals("Log Out")){
            windowManagerBasic.logOut();
        }
    }



}

