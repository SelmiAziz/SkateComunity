package view;

import Dao.EventDao;
import Dao.EventDemoDao;
import Dao.Factories.DaoFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.Event;
import utils.SceneManager;
import login.SessionManager;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class ManagerEventsPageView {
    @FXML
    private TextField eventNameField;

    @FXML
    private DatePicker eventDatePicker;

    @FXML
    private TextArea eventDescriptionArea;

    @FXML
    private TextField coinAmountField;

    @FXML
    private TextField maxRegistrationsField;

    @FXML
    private TableView<Event> eventTable;

    @FXML
    private TableColumn<Event, String> colTitolo;

    @FXML
    private TableColumn<Event, String> colDescrizione;

    @FXML
    private TableColumn<Event, Integer> colMonete;

    @FXML
    private TableColumn<Event, String> colPaese;

    @FXML
    private TableColumn<Event, String> colData;

    @FXML
    private TableColumn<Event, Integer> colIscrizioniAttuali;

    @FXML
    private TableColumn<Event, Integer> colIscrizioniMassime;

    @FXML
    private ChoiceBox<String> countryChoiceBox;

    private void populateCountryChoiceBox() {
        List<String> countries = Arrays.asList("Italy", "France", "Germany", "USA", "UK", "Spain", "Canada", "Japan");
        ObservableList<String> countryList = FXCollections.observableArrayList(countries);
        countryChoiceBox.setItems(countryList);
        countryChoiceBox.setValue("Italy"); // Imposta un valore di default
    }

    public void goToHomePage() throws IOException {
        SceneManager.getInstance().loadScene("ManagerHomePageView.fxml");
    }

    public void loadEvents(){
        EventDemoDao myRepo = EventDemoDao.getInstance();

        // Pulisci la lista esistente
        eventTable.getItems().clear();

        // Aggiungi gli eventi al TableView
        eventTable.getItems().addAll(myRepo.getAllEvents());
    }

    @FXML
    public void initialize() {
        // Imposta il cellValueFactory per ogni colonna
        colTitolo.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getName()));
        colDescrizione.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getDescription()));
        colData.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getDate()));
        colMonete.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getCoins()).asObject());
        colPaese.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getCountry()));
        colIscrizioniAttuali.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getCurrentRegistrations()).asObject());
        colIscrizioniMassime.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getMaxRegistrations()).asObject());
        populateCountryChoiceBox();
    }
    public void logOut() throws IOException {
        SessionManager.getInstance().terminateSession();
        SceneManager.getInstance().loadScene("AccessView.fxml");
    }
    @FXML
    private void createEvent() {
        String name = eventNameField.getText();
        String description = eventDescriptionArea.getText();
        String date = eventDatePicker.getValue() != null ? eventDatePicker.getValue().toString() : "";
        String country = countryChoiceBox.getValue();
        int coins, maxRegistrations;

        try {
            coins = Integer.parseInt(coinAmountField.getText());
            maxRegistrations = Integer.parseInt(maxRegistrationsField.getText());
        } catch (NumberFormatException e) {
            System.out.println("Invalid number format");
            return;
        }

        if (name.isEmpty() || description.isEmpty() || date.isEmpty() || country.isEmpty()) {
            System.out.println("All fields must be filled");
            return;
        }

        Event newEvent = new Event(name, description, date, coins, country, maxRegistrations, "marci");



        DaoFactory myFactory = DaoFactory.getInstance();
        EventDao repo = myFactory.createEventDao();
        repo.addEvent(newEvent);

        eventTable.getItems().add(newEvent);


    }


}
