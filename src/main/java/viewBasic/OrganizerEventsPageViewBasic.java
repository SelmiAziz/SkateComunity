package viewBasic;


import beans.EventBean;
import controls.CreateEventController;
import exceptions.EmptyFieldException;
import exceptions.EventAlreadyExistsException;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import utils.SceneManager;
import utils.SessionManager;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class OrganizerEventsPageViewBasic {
    @FXML private TextField eventNameField;
    @FXML private TextField monthField;
    @FXML private TextField dayField;
    @FXML private TextField yearField;
    @FXML private TextArea eventDescriptionArea;
    @FXML private TextField coinsAmountField;
    @FXML private TextField maxRegistrationsField;
    @FXML private ListView<String> eventList;
    @FXML private TextField locationField;
    @FXML private Label errorLabel;

    private final CreateEventController createEventController = new CreateEventController();



    private void validateFields(String name, String description, String date, String location) throws EmptyFieldException{
        if (name.isEmpty() || description.isEmpty() || date.isEmpty() || location.isEmpty()) {
            throw new EmptyFieldException("I campi per la creazione dell'evento devono essere tutti compilati!!!");
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
    private void createEvent() {
        String name = eventNameField.getText();
        String description = eventDescriptionArea.getText();
        String month = monthField.getText();
        String day = dayField.getText();
        String year = yearField.getText();
        String date = formatDate(month,day,year);
        String location = formatLocation(locationField.getText());
        int coinsRequired , maxRegistrations;

        try{
            validateFields(name, description,date,location);
            coinsRequired = Integer.parseInt(coinsAmountField.getText());
            maxRegistrations = Integer.parseInt(maxRegistrationsField.getText());
            createEventController.createEvent(new EventBean(name,description,date,location,coinsRequired,maxRegistrations));
            loadEvents();
        } catch (NumberFormatException e) {
            errorLabel.setText(e.getMessage());
        }catch(EventAlreadyExistsException e){
            errorLabel.setText(e.getMessage());
        }catch(EmptyFieldException e){
            errorLabel.setText(e.getMessage());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }

    public void loadEvents(){
        List<EventBean> availableEventsBean =  createEventController.organizerEvents();
        eventList.getItems().clear();
        for (EventBean eventBean : availableEventsBean) {
            String trickDisplay = String.format(
                    "<<Name Event: %s>>-<<Description: %s>>-<<Date: %s>>-<<location: %s>>-<<Coins Required: %d>>-<<CurrentRegistration: %d>>-<<MaxRegistration: %d>>",
                    eventBean.getName(),
                    eventBean.getDescription(),
                    formatDateToView(eventBean.getDate()),
                    formatLocationToView(eventBean.getLocation()),
                    eventBean.getCoins(),
                    eventBean.getCurrentRegistrations(),
                    eventBean.getMaxRegistrations()
            );
            eventList.getItems().add(trickDisplay);

        }
    }


    public void logOut()  {
        SessionManager.getInstance().terminateSession();
        try {
            SceneManager.getInstance().loadScene("viewFxml/AccessView.fxml");
        }catch (IOException e){
            System.err.println("Errore di I/O: " + e.getMessage());
            errorLabel.setText(e.getMessage());
        }
    }

    public void displayReviews(){

    }

    @FXML
    public void goToTricksPage(){
        try {
            SceneManager.getInstance().loadScene("viewFxmlBasic/OrganizerTricksPageViewBasic.fxml");
        }catch(IOException e){
            errorLabel.setText(e.getMessage());
        }
    }
    @FXML
    public void goToCompetitionsPage(){

    }



    public void goToHomePage()  {
        try {
            SceneManager.getInstance().loadScene("viewFxmlBasic/OrganizerHomePageViewBasic.fxml");
        }catch (IOException e){
            System.err.println("Errore di I/O: " + e.getMessage());
            errorLabel.setText(e.getMessage());
        }
    }



}
