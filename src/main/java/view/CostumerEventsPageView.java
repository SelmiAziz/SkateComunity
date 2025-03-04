package view;

import Dao.EventDao;
import Dao.EventDemoDao;
import beans.EventBean;
import controls.SignEventController;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.Event;
import utils.SceneManager;
import utils.SessionManager;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class CostumerEventsPageView {
    private final SignEventController signEventController = new SignEventController();
    private final SceneManager sceneManager = SceneManager.getInstance();

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
    @FXML private Label errorLabel;


    private EventBean selectedEvent;

    public void initialize() {
        // Imposta il cellValueFactory per ogni colonna
        colName.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getName()));

        colDescription.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getDescription()));

        colDate.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getDate()));
    }

    public void loadEvents() {

        coinsLabel.setText("120");

        eventTable.getItems().clear();

        eventTable.getItems().addAll(signEventController.showAllEvents());
    }

    @FXML
    public void searchEvents() {

    }

    // Metodo per gestire la selezione di un evento
    @FXML
    public void onEventSelected() {
        EventBean selected = eventTable.getSelectionModel().getSelectedItem();
        EventBean eventBeanDetails = signEventController.showEventDetail(selected);
        showEventDetails(eventBeanDetails);
    }

    // Mostra i dettagli dell'evento selezionato
    private void showEventDetails(EventBean eventBean) {
        if (eventBean != null) {
            selectedEvent = eventBean;
            eventNameLabel.setText(eventBean.getName());
            eventDescriptionLabel.setText(eventBean.getDescription());
            eventCoinsLabel.setText(""+ eventBean.getCoins());
            eventSeatsAvailableLabel.setText("Registrazioni massime: " + eventBean.getMaxRegistrations());

        }
    }

    @FXML
    public void submitRegistrationEvent(){
        //Da implementare
    }



    public void goToHomePage() {
        try {
            sceneManager.loadScene("CostumerHomePageView.fxml");
        }catch(IOException e){
            System.err.println("Errore di I/O: " + e.getMessage());
            errorLabel.setText(e.getMessage());
        }
    }

    public void logOut(){
        SessionManager.getInstance().terminateSession();
        try {
            sceneManager.loadScene("AccessView.fxml");
        }catch(IOException e){
            System.err.println("Errore di I/O: " + e.getMessage());
            errorLabel.setText(e.getMessage());
        }
    }
}
