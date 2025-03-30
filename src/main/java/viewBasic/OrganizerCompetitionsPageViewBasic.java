package viewBasic;

import beans.CompetitionBean;
import controls.CreateCompetitionController;
import exceptions.EmptyFieldException;
import exceptions.CompetitionAlreadyExistsException;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import utils.SceneManager;
import utils.SessionManager;

import java.io.IOException;
import java.sql.SQLException;
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

    private final CreateCompetitionController createCompetitionController = new CreateCompetitionController();


    private void validateFields(String name, String description, String date, String location) throws EmptyFieldException{
        if (name.isEmpty() || description.isEmpty() || date.isEmpty() || location.isEmpty()) {
            throw new EmptyFieldException("I campi per la creazione della competizione devono essere tutti compilati!!!");
        }
    }


    String formatDate(String month, String day, String year){
        return day+"-"+month+"-20"+year;
    }

    String formatDateToView(String date){
        String[] datArr = date.split("-");
        return datArr[1]+"-"+datArr[0]+"-"+datArr[2];
    }


    String formatLocation(String location){return location.replace("/", "-");}

    String formatLocationToView(String location){return location.replace("-", "/");}

    @FXML
    private void createCompetition() {
        String name = competitionNameField.getText();
        String description = competitionDescriptionArea.getText();
        String month = monthField.getText();
        String day = dayField.getText();
        String year = yearField.getText();
        String date = formatDate(month, day, year);
        String location = formatLocation(locationField.getText());
        int coinsRequired, maxRegistrations;

        try{
            validateFields(name, description, date, location);
            coinsRequired = Integer.parseInt(coinsAmountField.getText());
            maxRegistrations = Integer.parseInt(maxRegistrationsField.getText());
            createCompetitionController.createCompetition(new CompetitionBean(name, description, date, location, coinsRequired, maxRegistrations));
            loadCompetitions();
        } catch (NumberFormatException e) {
            errorLabel.setText(e.getMessage());
        } catch(CompetitionAlreadyExistsException e){
            errorLabel.setText(e.getMessage());
        } catch(EmptyFieldException e){
            errorLabel.setText(e.getMessage());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void loadCompetitions(){
        List<CompetitionBean> availableCompetitionsBean = createCompetitionController.organizerCompetitions();
        competitionList.getItems().clear();
        for (CompetitionBean competitionBean : availableCompetitionsBean) {
            String competitionDisplay = String.format(
                    "<<Name Competition: %s>>-<<Description: %s>>-<<Date: %s>>-<<Location: %s>>-<<Coins Required: %d>>-<<CurrentRegistration: %d>>-<<MaxRegistration: %d>>",
                    competitionBean.getName(),
                    competitionBean.getDescription(),
                    formatDateToView(competitionBean.getDate()),
                    formatLocationToView(competitionBean.getLocation()),
                    competitionBean.getCoins(),
                    competitionBean.getCurrentRegistrations(),
                    competitionBean.getMaxRegistrations()
            );
            competitionList.getItems().add(competitionDisplay);
        }
    }

    public void logOut()  {
        SessionManager.getInstance().terminateSession();
        try {
            SceneManager.getInstance().loadScene("viewFxml/AccessView.fxml");
        } catch (IOException e){
            System.err.println("Errore di I/O: " + e.getMessage());
            errorLabel.setText(e.getMessage());
        }
    }

    public void displayReviews(){

    }

    @FXML
    public void goToCommissionsPage(){

    }

    @FXML
    public void goToTricksPage(){
        try {
            SceneManager.getInstance().loadScene("viewFxmlBasic/OrganizerTricksPageViewBasic.fxml");
        } catch(IOException e){
            errorLabel.setText(e.getMessage());
        }
    }

    @FXML
    public void goToCompetitionsPage(){

    }

    public void goToHomePage()  {
        try {
            SceneManager.getInstance().loadScene("viewFxmlBasic/OrganizerHomePageViewBasic.fxml");
        } catch (IOException e){
            System.err.println("Errore di I/O: " + e.getMessage());
            errorLabel.setText(e.getMessage());
        }
    }
}

