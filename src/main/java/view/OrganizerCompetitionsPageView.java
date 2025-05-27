package view;

import beans.CompetitionBean;
import controls.CreateCompetitionController;
import exceptions.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import utils.WindowManager;

import java.io.IOException;

public class OrganizerCompetitionsPageView {
   WindowManager windowManager = WindowManager.getInstance();
   DateValidator dateValidator = new DateValidator();

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





    @FXML
    private void createCompetition() {
        String name = competitionNameField.getText();
        String description = competitionDescriptionArea.getText();
        String location = locationField.getText();
        int coinsRequired;
        int maxRegistrations;
        String date = dateField.getText();
        coinsRequired = coinsSpinner.getValue();
        maxRegistrations = maxRegistrationsSpinner.getValue();

        try {
            dateValidator.validaDate(date);
            validateFields(name, description, date, location);
            createCompetitionController.createCompetition(windowManager.getAuthBean().getToken(),new CompetitionBean(name, description, date, location, coinsRequired, maxRegistrations));
            loadCompetitions();
        } catch (WrongFormatException | CompetitionAlreadyExistsException | EmptyFieldException | DataAccessException e) {
            errorLabel.setText(e.getMessage());
        }catch(SessionExpiredException _ ){
            windowManager.logOut();

        }
    }

    public void loadCompetitions() {
        try {
            competitionTable.getItems().clear();
            competitionTable.getItems().addAll(createCompetitionController.organizerCompetitions(windowManager.getAuthBean().getToken()));
        }catch(SessionExpiredException _){
                windowManager.logOut();
        }catch(DataAccessException e){
            errorLabel.setText(e.getMessage());
        }
    }




    @FXML
    public void goToTricksPage() {
        try {
            windowManager.goToLearn();
        } catch (IOException e) {
            errorLabel.setText(e.getMessage());
        }
    }

    public void logOut() {
        windowManager.logOut();
    }

    public void goToHomePage() {
        try {
            windowManager.goToHomePage();
        } catch (IOException e) {
            errorLabel.setText(e.getMessage());
        }
    }

    public void goToSkateboardsPage(){
        try {
            windowManager.goToSkateboards();
        } catch (IOException e) {
            errorLabel.setText(e.getMessage());
        }
    }
}

