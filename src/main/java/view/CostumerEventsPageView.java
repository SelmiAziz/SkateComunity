package view;

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


public class CostumerEventsPageView {
    private final SignEventController signEventController = new SignEventController();
    private final SceneManager sceneManager = SceneManager.getInstance();
    //private final AccountInfoSessionManager accountInfoSession = AccountInfoSessionManager.getInstance();

    @FXML private TableView<EventBean> eventTable;
    @FXML private TableColumn<EventBean, String> colName;
    @FXML private TableColumn<EventBean, String> colDescription;
    @FXML private TableColumn<EventBean, String> colDate;
    @FXML private TextField nationalitySearch;
    @FXML private TextField dateSearch;
    @FXML private Label eventDescriptionLabel;
    @FXML private Label eventCoinsLabel;
    @FXML private Label eventNameLabel;
    @FXML private Label eventSeatsAvailableLabel;
    @FXML private Label coinsLabel;
    @FXML private Label usernameLabel;
    @FXML private Label errorLabel;


    private EventBean selectedEventBean;

    public void initialize() {
        // Imposta il cellValueFactory per ogni colonna
        colName.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getName()));

        colDescription.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getDescription()));

        colDate.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getDate()));

       updateUserInfo();
    }


    public void loadEvents() {

        eventTable.getItems().clear();
        eventTable.getItems().addAll(signEventController.allAvailableEvents());
    }

    @FXML
    public void searchEvents() {
        String date = dateSearch.getText();
        String country = nationalitySearch.getText();
        EventBean eventBean = new EventBean(date,country);
        eventTable.getItems().clear();
        eventTable.getItems().addAll(signEventController.searchEventByDateAndCountry(eventBean));

    }

    // Metodo per gestire la selezione di un evento
    @FXML
    public void onEventSelected() {
        EventBean selected = eventTable.getSelectionModel().getSelectedItem();
        EventBean eventBeanDetails = signEventController.eventDetails(selected);
        showEventDetails(eventBeanDetails);
    }

    // Mostra i dettagli dell'evento selezionato
    private void showEventDetails(EventBean eventBean) {
        if (eventBean != null) {
            selectedEventBean = eventBean;
            eventNameLabel.setText(eventBean.getName());
            eventDescriptionLabel.setText(eventBean.getDescription());
            eventCoinsLabel.setText(""+ eventBean.getCoins());
            eventSeatsAvailableLabel.setText(""+eventBean.getAvailableRegistrations());

        }
    }

    @FXML
    public void submitSignToEvent(){
        try {
            signEventController.signToEvent(selectedEventBean);
            updateUserInfo();
            onEventSelected();
        } catch (UserAlreadySignedEvent | InsufficientCoinsException | NoAvailableSeats e) {
            errorLabel.setText(e.getMessage());
            System.err.println(e.getMessage());
        }
    }



    public void goToHomePage() {
        try {
            sceneManager.loadScene("viewFxml/CostumerHomePageView.fxml");
        }catch(IOException e){
            System.err.println("Errore di I/O: " + e.getMessage());
            errorLabel.setText(e.getMessage());
        }
    }

    void updateUserInfo(){
        UserInfo userInfo = signEventController.getCurrentUserInfo();
        usernameLabel.setText(userInfo.getUsername());
        coinsLabel.setText(String.valueOf(userInfo.getCoins()));
    }

    public void logOut(){
        //SessionManager.getInstance().terminateSession();
        try {
            sceneManager.loadScene("viewFxml/AccessView.fxml");
        }catch(IOException e){
            System.err.println("Errore di I/O: " + e.getMessage());
            errorLabel.setText(e.getMessage());
        }
    }
}
