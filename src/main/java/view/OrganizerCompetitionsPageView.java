package view;

import beans.CompetitionBean;
import controls.CreateCompetitionController;
import exceptions.EmptyFieldException;
import exceptions.CompetitionAlreadyExistsException;
import exceptions.WrongFormatException;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import utils.SceneManager;
import utils.SessionManager;

import java.io.IOException;
import java.sql.SQLException;

public class OrganizerCompetitionsPageView {
    @FXML private TextField competitionNameField;
    @FXML private TextField locationField;
    @FXML private TextField dateField;
    @FXML private TextArea competitionDescriptionArea;
    @FXML private TableView<CompetitionBean> competitionTable;
    @FXML private TableColumn<CompetitionBean, String> colName;
    @FXML private TableColumn<CompetitionBean, String> colDescription;
    @FXML private TableColumn<CompetitionBean, String> colDate;
    @FXML private TableColumn<CompetitionBean, String> colLocation;
    @FXML private TableColumn<CompetitionBean, String> colCoinsRequired;
    @FXML private TableColumn<CompetitionBean, String> colMaxRegistrations;
    @FXML private TableColumn<CompetitionBean, String> colCurrentRegistrations;
    @FXML private Label errorLabel;
    @FXML private Spinner<Integer> coinsSpinner;
    @FXML private Spinner<Integer> maxRegistrationsSpinner;

    private final CreateCompetitionController createCompetitionController = new CreateCompetitionController();

    @FXML
    public void initialize() {
        colName.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getName()));

        colDescription.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getDescription()));

        colDate.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getDate()));

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

    private void validateFields(String name, String description, String date, String location) throws EmptyFieldException {
        if (name.isEmpty() || description.isEmpty() || date.isEmpty() || location.isEmpty()) {
            throw new EmptyFieldException("I campi per la creazione della competizione devono essere tutti compilati!!!");
        }
    }

    private void validaDate(String data) throws WrongFormatException {
        if (data == null || !data.matches("^\\d{2}/\\d{2}/\\d{4}$")) {
            throw new WrongFormatException("Formato non valido (deve essere dd/mm/yyyy)");
        }

        String[] parts = data.split("/");
        int day = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]);
        int year = Integer.parseInt(parts[2]);

        if (month < 1 || month > 12) {
            throw new WrongFormatException("Mese non valido: " + month);
        }

        int[] daysInMonth = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };

        if (month == 2 && ((year % 4 == 0 && year % 100 != 0) || (year % 400 == 0))) {
            daysInMonth[1] = 29;
        }

        if (day < 1 || day > daysInMonth[month - 1]) {
            throw new WrongFormatException("Giorno non valido: " + day + " per il mese " + month);
        }
    }

    public void doN(){
        coinsSpinner.setVisible(false);
    }





    @FXML
    private void createCompetition() {
        String name = competitionNameField.getText();
        String description = competitionDescriptionArea.getText();
        String location = locationField.getText();
        int coinsRequired, maxRegistrations;
        String date = dateField.getText();
        coinsRequired = coinsSpinner.getValue();
        maxRegistrations = maxRegistrationsSpinner.getValue();

        try {
            validaDate(date);
            validateFields(name, description, date, location);
            createCompetitionController.createCompetition(new CompetitionBean(name, description, date, location, coinsRequired, maxRegistrations));
            loadCompetitions();
        } catch (WrongFormatException | CompetitionAlreadyExistsException | EmptyFieldException | SQLException e) {
            errorLabel.setText(e.getMessage());
        }
    }

    public void loadCompetitions() {
        competitionTable.getItems().clear();
        competitionTable.getItems().addAll(createCompetitionController.organizerCompetitions());
    }

    @FXML
    public void goToCompetitionsPage() {
    }

    public void goToCommissionsPage(){

    }


    @FXML
    public void goToTricksPage() {
        try {
            SceneManager.getInstance().loadScene("viewFxml/OrganizerTricksPageView.fxml");
        } catch (IOException e) {
            errorLabel.setText(e.getMessage());
        }
    }

    public void logOut() {
        SessionManager.getInstance().terminateSession();
        try {
            SceneManager.getInstance().loadScene("viewFxml/AccessView.fxml");
        } catch (IOException e) {
            errorLabel.setText(e.getMessage());
        }
    }

    public void goToHomePage() {
        try {
            SceneManager.getInstance().loadScene("viewFxml/OrganizerHomePageView.fxml");
        } catch (IOException e) {
            errorLabel.setText(e.getMessage());
        }
    }

    public void goToSkateboardsPage(){
        SessionManager.getInstance().terminateSession();
        try {
            SceneManager.getInstance().loadScene("viewFxml/OrganizerSkateboardsPageView.fxml");
        } catch (IOException e) {
            errorLabel.setText(e.getMessage());
        }
    }
}

