package view;

import beans.EventBean;
import beans.EventRegistrationBean;
import beans.UserInfo;
import controls.SignEventController;
import exceptions.InsufficientCoinsException;
import exceptions.NoAvailableSeats;
import exceptions.UserAlreadySignedEvent;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import utils.SceneManager;

import java.io.IOException;


public class CustomerEventsPageView {
    private final SignEventController signEventController = new SignEventController();
    private final SceneManager sceneManager = SceneManager.getInstance();

    @FXML private TableView<EventBean> eventTable;
    @FXML private TableColumn<EventBean, String> colName;
    @FXML private TableColumn<EventBean, String> colDescription;
    @FXML private TableColumn<EventBean, String> colDate;
    @FXML private TextField locationSearch;
    @FXML private DatePicker datePicker;
    @FXML private Label eventDescriptionLabel;
    @FXML private Label eventCoinsLabel;
    @FXML private Label eventNameLabel;
    @FXML private Label eventSeatsAvailableLabel;
    @FXML private Label coinsLabel;
    @FXML private Label usernameLabel;
    @FXML private Label errorLabel;
    @FXML private Label registrationCodeLabel;
    @FXML private Label assignedSeatLabel;
    @FXML private Pane provaPane;



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

        provaPane.setVisible(false);
    }


    public void loadEvents() {

        eventTable.getItems().clear();
        eventTable.getItems().addAll(signEventController.allAvailableEvents());
    }

    public void reviewEvent(){

    }

    public String formatFromDataPicker(String date){
        String[] parts = date.split("-");
        return parts[2] + "-" + parts[1] + "-" + parts[0];
    }

    @FXML
    public void searchEvents() {
        String date = formatFromDataPicker(datePicker.getValue().toString());
        String location = locationSearch.getText();
        System.out.println(date+location);
        EventBean eventBean = new EventBean(date,location);
        eventTable.getItems().clear();
        eventTable.getItems().addAll(signEventController.searchEventByDateAndLocation(eventBean));

    }

    @FXML
    public void onEventSelected() {
        EventBean selected = eventTable.getSelectionModel().getSelectedItem();
        EventBean eventBeanDetails = signEventController.eventDetails(selected);
        showEventDetails(eventBeanDetails);
    }

    // Mostra i dettagli dell'evento selezionato
    private void showEventDetails(EventBean eventBean) {
        if (eventBean != null) {
            eventNameLabel.setText(eventBean.getName());
            eventDescriptionLabel.setText(eventBean.getDescription());
            eventCoinsLabel.setText(""+ eventBean.getCoins());
            eventSeatsAvailableLabel.setText(""+eventBean.getAvailableRegistrations());

        }
    }

    @FXML
    public void submitSignToEvent(){
        try {
            EventBean selectedEventBean = eventTable.getSelectionModel().getSelectedItem();
            EventRegistrationBean eventRegistrationBean= signEventController.signToEvent(selectedEventBean);
            onEventSelected();
            registrationCodeLabel.setText("Il codice della registrazione:"+eventRegistrationBean.getRegistrationCode());
            assignedSeatLabel.setText("Il posto assegnato è:"+eventRegistrationBean.getAssignedSeat());
            provaPane.setVisible(true);
        } catch (UserAlreadySignedEvent | InsufficientCoinsException | NoAvailableSeats e) {
            errorLabel.setText(e.getMessage());
            System.err.println(e.getMessage());
        }
    }
    @FXML
    public void goToCompetitionsPage(){

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
