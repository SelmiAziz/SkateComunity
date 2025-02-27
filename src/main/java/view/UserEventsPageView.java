package view;

import Dao.EventDao;
import Dao.Factories.DaoFactory;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.Event;
import model.Costumer;
import utils.SceneManager;
import login.SessionManager;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class UserEventsPageView {

    @FXML
    private TableView<Event> eventTable;

    @FXML
    private TableColumn<Event, String> colTitolo;

    @FXML
    private TableColumn<Event, String> colDescrizione;

    @FXML
    private TableColumn<Event, String> colData;

    @FXML
    private TextField nationalitySearch;

    @FXML
    private TextField dateSearch;

    @FXML
    private Label eventDescriptionLabel;

    @FXML
    private Label eventCoinsLabel;

    @FXML
    private Label eventNameLabel;

    @FXML
    private Label eventSeatsLabel;

    @FXML
    private Label eventMaxRegistrationsLabel;


    private Event selectedEvent;

    public void initialize() {
        // Imposta il cellValueFactory per ogni colonna
        colTitolo.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getName()));
        colDescrizione.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getDescription()));
        colData.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getDate()));

        // Aggiungi il listener per la selezione dell'evento
        eventTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> onEventSelected()
        );
    }

    public void loadEvents() {
        DaoFactory myFactory = DaoFactory.getInstance();
        EventDao repo = myFactory.createEventDao();


        // Pulisci la lista esistente
        eventTable.getItems().clear();

        // Aggiungi gli eventi al TableView
        eventTable.getItems().addAll(repo.getAllEvents());
    }

    @FXML
    public void searchEvents() {
        String nationality = nationalitySearch.getText();
        String date = dateSearch.getText();

        DaoFactory myFactory = DaoFactory.getInstance();
        EventDao repo = myFactory.createEventDao();
        List<Event> filteredEvents = repo.getAllEvents().stream()
                .filter(event -> (nationality.isEmpty() || event.getCountry().equalsIgnoreCase(nationality)) &&
                        (date.isEmpty() || event.getDate().equalsIgnoreCase(date)))
                .collect(Collectors.toList());

        // Pulisci la lista esistente e aggiungi gli eventi filtrati
        eventTable.getItems().clear();
        eventTable.getItems().addAll(filteredEvents);
    }

    // Metodo per gestire la selezione di un evento
    @FXML
    public void onEventSelected() {
        Event selected = eventTable.getSelectionModel().getSelectedItem();
        showEventDetails(selected);
    }

    // Mostra i dettagli dell'evento selezionato
    private void showEventDetails(Event event) {
        if (event != null) {
            selectedEvent = event;
            eventNameLabel.setText(event.getName());
            eventDescriptionLabel.setText(event.getDescription());
            eventCoinsLabel.setText(""+ event.getCoins());
            eventSeatsLabel.setText("Registrazioni massime: " + event.getMaxRegistrations());

        }
    }

    @FXML
    public void submitRegistrationEvent(){

    }



    public void goToHomePage() throws IOException {
        SessionManager sessionManager = SessionManager.getInstance();
        SceneManager.getInstance().loadScene("UserHomePageView.fxml");
    }

    public void logOut() throws IOException {
        SessionManager.getInstance().terminateSession();
        SceneManager.getInstance().loadScene("AccessView.fxml");
    }
}
