package view;


import beans.EventBean;
import beans.UserInfo;
import controls.CreateEventController;
import exceptions.EmptyFieldException;
import exceptions.EventAlreadyExistsException;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import utils.SceneManager;
import utils.SessionManager;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class OrganizerEventsPageView {
    @FXML private TextField eventNameField;
    @FXML private DatePicker eventDatePicker;
    @FXML private TextArea eventDescriptionArea;
    @FXML private TableView<EventBean> eventTable;
    @FXML private TableColumn<EventBean, String> colName;
    @FXML private TableColumn<EventBean, String> colDescription;
    @FXML private TableColumn<EventBean, String> colDate;
    @FXML private TableColumn<EventBean, String> colCountry;
    @FXML private TableColumn<EventBean, String> colCoinsRequired;
    @FXML private TableColumn<EventBean, String> colMaxRegistrations;
    @FXML private TableColumn<EventBean, String> colCurrentRegistrations;
    @FXML private ChoiceBox<String> countryChoiceBox;
    @FXML private Label usernameLabel;
    @FXML private Label coinsLabel;
    @FXML private Label errorLabel;
    @FXML private Spinner<Integer> coinsSpinner;
    @FXML private Spinner<Integer> maxRegistrationsSpinner;

    private final CreateEventController createEventController = new CreateEventController();

    @FXML
    public void initialize() {
        System.out.println("Prova");
        //maybe here you can apply a formatter
        colName.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getName()));

        colDescription.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getDescription()));

        colDate.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getDate()));

        colCountry.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getCountry()));

        colCoinsRequired.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getCoins())));

        colCurrentRegistrations.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getCurrentRegistrations())));

        colMaxRegistrations.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getMaxRegistrations())));
        coinsSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1000, 5));
        maxRegistrationsSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 1000, 5));
        populateCountryChoiceBox();
        updateUserInfo();
    }

    private void validateFields(String name, String description, String date, String country) throws EmptyFieldException{
        if (name.isEmpty() || description.isEmpty() || date.isEmpty() || country.isEmpty()) {
            throw new EmptyFieldException("I campi per la creazione dell'evento devono essere tutti compilati!!!");
        }
    }


    @FXML
    private void createEvent() {
        String name = eventNameField.getText();
        String description = eventDescriptionArea.getText();
        String date = eventDatePicker.getValue() != null ? eventDatePicker.getValue().toString() : "";
        String country = countryChoiceBox.getValue();
        int coinsRequired , maxRegistrations;

        try{
            validateFields(name, description,date,country);
            coinsRequired = coinsSpinner.getValue();
            maxRegistrations =  maxRegistrationsSpinner.getValue();
            createEventController.createEvent(new EventBean(name,description,date,country,coinsRequired,maxRegistrations));
            loadEvents();
        } catch (NumberFormatException e) {
            System.err.println("Invalid number format");
            errorLabel.setText(e.getMessage());
        }catch(EventAlreadyExistsException e){
            System.err.println("Errore di I/O: " + e.getMessage());
            errorLabel.setText(e.getMessage());
        }catch(EmptyFieldException e){
            System.err.println(e.getMessage());
            errorLabel.setText(e.getMessage());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }

    public void loadEvents(){
        eventTable.getItems().clear();
        eventTable.getItems().addAll(createEventController.organizerEvents());
    }

    public void updateUserInfo(){
        UserInfo userInfo = createEventController.getCurrentUserInfo();
        usernameLabel.setText(userInfo.getUsername());
        coinsLabel.setText(String.valueOf(userInfo.getCoins()));
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

    public void goToHomePage()  {
        try {
            SceneManager.getInstance().loadScene("viewFxml/OrganizerHomePageView.fxml");
        }catch (IOException e){
            System.err.println("Errore di I/O: " + e.getMessage());
            errorLabel.setText(e.getMessage());
        }
    }
    private void populateCountryChoiceBox() {
        //maybe here you can use an enumeration
        List<String> countries = Arrays.asList("Italy", "France", "Germany", "USA", "UK", "Spain", "Canada", "Japan");
        ObservableList<String> countryList = FXCollections.observableArrayList(countries);
        countryChoiceBox.setItems(countryList);
        countryChoiceBox.setValue("Italy");
    }


}
