package viewBasic;

import beans.WalletBean;
import beans.EventBean;
import beans.EventRegistrationBean;
import controls.SignEventController;
import exceptions.EmptyFieldException;
import exceptions.InsufficientCoinsException;
import exceptions.NoAvailableSeats;
import exceptions.UserAlreadySignedEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import utils.SceneManager;

import java.io.IOException;
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
    @FXML private Label generateCodeLabel;
    @FXML private Label assignedSeatLabel;
    @FXML private TextField eventNameField;
    @FXML private Label errorLabel;
    @FXML private Label eventDescriptionLabel;
    @FXML private Label eventSeatsAvailableLabel;
    @FXML private Label eventCoinsLabel;
    @FXML private Label eventNameLabel;
    @FXML private ListView<String> eventList;


    public void initialize() {
        List<EventBean> availableEventsBean =  signEventController.allAvailableEvents();
        loadEvents( availableEventsBean);
        updateWalletInfo();
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
        WalletBean walletBean = signEventController.costumerInfo();
        coinsLabel.setText(""+walletBean.getBalance());
    }

    public void eventDetails(){
        try {
            String eventName = eventNameField.getText();
            if(eventName.isEmpty()){throw new EmptyFieldException("Campo nome evento risulta vuoto!!");}
            EventBean eventBean = new EventBean(eventName);
            EventBean eventBeanDetailed = signEventController.eventDetails(eventBean);
            eventNameLabel.setText(eventBeanDetailed.getName());
            eventCoinsLabel.setText(""+eventBeanDetailed.getCoins());
            eventDescriptionLabel.setText(eventBeanDetailed.getDescription());
            eventSeatsAvailableLabel.setText(""+eventBeanDetailed.getAvailableRegistrations());


        }catch(EmptyFieldException e){
            errorLabel.setText(e.getMessage());
        }
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
    String formatLocation(String location){return location.replace("/","-");}

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
            System.out.println("Prova");
            try {
                String eventName = eventNameField.getText();
                System.out.println(eventName);
                if(eventName.isEmpty()) throw  new EmptyFieldException("Campo nome evento risulta vuoto!!!");
                EventBean eventBean = new EventBean(eventName);
                EventRegistrationBean eventRegistrationBean = signEventController.signToEvent(eventBean);
                assignedSeatLabel.setText(eventRegistrationBean.getAssignedSeat());
                generateCodeLabel.setText(eventRegistrationBean.getAssignedSeat());
            } catch (UserAlreadySignedEvent | InsufficientCoinsException | NoAvailableSeats e) {
                errorLabel.setText(e.getMessage());
            }
        }catch(EmptyFieldException e){
            errorLabel.setText(e.getMessage());
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
