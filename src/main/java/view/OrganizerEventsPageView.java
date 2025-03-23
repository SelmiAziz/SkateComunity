package view;


import beans.EventBean;
import beans.UserInfo;
import controls.CreateEventController;
import exceptions.EmptyFieldException;
import exceptions.EventAlreadyExistsException;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import utils.SceneManager;
import utils.SessionManager;

import java.io.IOException;
import java.sql.SQLException;

public class OrganizerEventsPageView {
    @FXML private TextField eventNameField;
    @FXML private TextField locationField;
    @FXML private DatePicker eventDatePicker;
    @FXML private TextArea eventDescriptionArea;
    @FXML private TableView<EventBean> eventTable;
    @FXML private TableColumn<EventBean, String> colName;
    @FXML private TableColumn<EventBean, String> colDescription;
    @FXML private TableColumn<EventBean, String> colDate;
    @FXML private TableColumn<EventBean, String> colLocation;
    @FXML private TableColumn<EventBean, String> colCoinsRequired;
    @FXML private TableColumn<EventBean, String> colMaxRegistrations;
    @FXML private TableColumn<EventBean, String> colCurrentRegistrations;
    @FXML private Label errorLabel;
    @FXML private Spinner<Integer> coinsSpinner;
    @FXML private Spinner<Integer> maxRegistrationsSpinner;

    private final CreateEventController createEventController = new CreateEventController();

    @FXML
    public void initialize() {
        //maybe here you can apply a formatter
        colName.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getName()));

        colDescription.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getDescription()));

        colDate.setCellValueFactory(cellData ->
                new SimpleStringProperty(formatDateToView(cellData.getValue().getDate())));

        colLocation.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getLocation()));

        colCoinsRequired.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getCoins())));

        colCurrentRegistrations.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getCurrentRegistrations())));

        colMaxRegistrations.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getMaxRegistrations())));
        coinsSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1000, 5));
        maxRegistrationsSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 1000, 5));
    }


    private void validateFields(String name, String description, String date, String location) throws EmptyFieldException{
        if (name.isEmpty() || description.isEmpty() || date.isEmpty() || location.isEmpty()) {
            throw new EmptyFieldException("I campi per la creazione dell'evento devono essere tutti compilati!!!");
        }
    }


    public String formatFromDatePicker(String date){
        String[] parts = date.split("-");
        return parts[2] + "-" + parts[1] + "-" + parts[0];
    }

    String formatDateToView(String date){
        return date.replace("-", "/");
    }

    @FXML
    private void createEvent() {
        String name = eventNameField.getText();
        String description = eventDescriptionArea.getText();
        String date = formatFromDatePicker(eventDatePicker.getValue().toString());
        String location =  locationField.getText();
        int coinsRequired , maxRegistrations;

        try{
            validateFields(name, description,date,location);
            coinsRequired = coinsSpinner.getValue();
            maxRegistrations =  maxRegistrationsSpinner.getValue();
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
        eventTable.getItems().clear();
        eventTable.getItems().addAll(createEventController.organizerEvents());
    }

    @FXML
    public void goToCompetitionsPage(){

    }

    @FXML
    public void goToTricksPage(){
        try {
            SceneManager.getInstance().loadScene("viewFxml/OrganizerTricksPageView.fxml");
        }catch(IOException e){
            errorLabel.setText(e.getMessage());
        }
    }


    public void logOut()  {
        SessionManager.getInstance().terminateSession();
        try {
            SceneManager.getInstance().loadScene("viewFxml/AccessView.fxml");
        }catch (IOException e){
            errorLabel.setText(e.getMessage());
        }
    }

    public void goToHomePage()  {
        try {
            SceneManager.getInstance().loadScene("viewFxml/OrganizerHomePageView.fxml");
        }catch (IOException e){
            errorLabel.setText(e.getMessage());
        }
    }



}
