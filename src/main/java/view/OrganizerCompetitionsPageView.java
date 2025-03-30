package view;

import beans.CompetitionBean;
import controls.CreateCompetitionController;
import exceptions.EmptyFieldException;
import exceptions.CompetitionAlreadyExistsException;
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
    @FXML private DatePicker competitionDatePicker;
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

    private void validateFields(String name, String description, String date, String location) throws EmptyFieldException {
        if (name.isEmpty() || description.isEmpty() || date.isEmpty() || location.isEmpty()) {
            throw new EmptyFieldException("I campi per la creazione della competizione devono essere tutti compilati!!!");
        }
    }

    public String formatFromDatePicker(String date) {
        String[] parts = date.split("-");
        return parts[2] + "-" + parts[1] + "-" + parts[0];
    }

    String formatDateToView(String date) {
        return date.replace("-", "/");
    }

    @FXML
    private void createCompetition() {
        String name = competitionNameField.getText();
        String description = competitionDescriptionArea.getText();
        String date = formatFromDatePicker(competitionDatePicker.getValue().toString());
        String location = locationField.getText();
        int coinsRequired, maxRegistrations;

        try {
            validateFields(name, description, date, location);
            coinsRequired = coinsSpinner.getValue();
            maxRegistrations = maxRegistrationsSpinner.getValue();
            createCompetitionController.createCompetition(new CompetitionBean(name, description, date, location, coinsRequired, maxRegistrations));
            loadCompetitions();
        } catch (NumberFormatException e) {
            errorLabel.setText(e.getMessage());
        } catch (CompetitionAlreadyExistsException e) {
            errorLabel.setText(e.getMessage());
        } catch (EmptyFieldException e) {
            errorLabel.setText(e.getMessage());
        } catch (SQLException e) {
            throw new RuntimeException(e);
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
}

