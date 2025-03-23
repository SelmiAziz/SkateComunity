package viewBasic;

import beans.EventBean;
import beans.UserInfo;
import controls.SignEventController;
import exceptions.InsufficientCoinsException;
import exceptions.NoAvailableSeats;
import exceptions.UserAlreadySignedEvent;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import utils.SceneManager;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.List;


public class CustomerEventsPageViewBasic {
    private final SignEventController signEventController = new SignEventController();
    private final SceneManager sceneManager = SceneManager.getInstance();


    @FXML private TextField locationSearch;
    @FXML private TextField  monthField;
    @FXML private TextField dayField;
    @FXML private TextField yearField;
    @FXML private Label coinsLabel;
    @FXML private Label usernameLabel;
    @FXML private Label errorLabel;
    @FXML private ListView<String> eventList;

    private EventBean selectedEventBean;

    public void initialize() {
        List<EventBean> availableEventsBean =  signEventController.allAvailableEvents();
        loadEvents( availableEventsBean);
    }

    String formatLocationToView(String location){
        if(location == null) return "";
        return location.replace("-", "/");
    }

    String formatDateToView(String date){
        String[] datArr = date.split("-");
        return datArr[1]+"-"+datArr[0]+"-"+datArr[2];
    }

    public void submitReview(){

    }

    public void eventDetails(){

    }

    public void loadEvents(List<EventBean> toLoadEvents) {
        eventList.getItems().clear();
        for (EventBean eventBean : toLoadEvents) {
            String trickDisplay = String.format(
                    "<<Name Event: %s>>-<<Date: %s>>-<<Location: %s>>",
                    eventBean.getName(),
                    formatDateToView(eventBean.getDate()),
                    formatLocationToView(eventBean.getLocation())
            );
            eventList.getItems().add(trickDisplay);

        }
    }



    String formatDate(String month, String day, String year){
        return day+"-"+month+"-20"+year;
    }
    String formatLocation(String location){return location.replace("/","-");};

    @FXML
    public void searchEvents() {
        String date = formatDate(monthField.getText(), dayField.getText(), yearField.getText());
        String location = formatLocation(locationSearch.getText());
        EventBean eventBean = new EventBean(date,location);
        List<EventBean> filteredEvents = signEventController.searchEventByDateAndLocation(eventBean);
        loadEvents(filteredEvents);

    }


    // Mostra i dettagli dell'evento selezionato


    @FXML
    public void submitSignToEvent(){
        try {
            signEventController.signToEvent(selectedEventBean);
        } catch (UserAlreadySignedEvent | InsufficientCoinsException | NoAvailableSeats e) {
            errorLabel.setText(e.getMessage());
            System.err.println(e.getMessage());
        }
    }



    public void goToHomePage() {
        try {
            sceneManager.loadScene("viewFxmlBasic/CustomerHomePageViewBasic.fxml");
        }catch(IOException e){
            errorLabel.setText(e.getMessage());
        }
    }

    @FXML
    public void goToTricksPage(){
        try {
            sceneManager.loadScene("viewFxmlBasic/CustomerTricksPageViewBasic.fxml");
        }catch(IOException e){
            errorLabel.setText(e.getMessage());
        }
    }

    @FXML
    public void goToCompetitionsPage(){
    }



    public void logOut(){
        try {
            sceneManager.loadScene("viewFxmlBasic/AccessViewBasic.fxml");
        }catch(IOException e){
            errorLabel.setText(e.getMessage());
        }
    }
}
