package view;


import beans.TrickBean;
import controls.LearnTrickController;
import exceptions.EmptyFieldException;
import exceptions.SessionExpiredException;
import exceptions.WrongFormatException;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import utils.WindowManager;

import java.io.IOException;
import java.util.List;

public class OrganizerTricksPageView {

    WindowManager windowManager = WindowManager.getInstance();

    @FXML Label errorLabel;
    @FXML private TableView<TrickBean> trickTable;
    @FXML private TableColumn<TrickBean, String> colTrickName;
    @FXML private TableColumn<TrickBean, String> colDescription;
    @FXML private TableColumn<TrickBean, String> colDifficulty;
    @FXML private TableColumn<TrickBean, String> colCategory;
    @FXML private Slider difficultySlider;
    @FXML private TextField trickNameTextField;
    @FXML private ToggleGroup trickGroup;
    @FXML private RadioButton flatRadio, grindRadio, rampRadio;
    @FXML private TextArea descriptionTextArea;
    @FXML private TextField dateField;
    @FXML private Label difficultyLabel;

    LearnTrickController learnTrickController = new LearnTrickController();

    @FXML
    public void initialize() {
        trickGroup = new ToggleGroup();
        flatRadio.setToggleGroup(trickGroup);
        grindRadio.setToggleGroup(trickGroup);
        rampRadio.setToggleGroup(trickGroup);

        colTrickName.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getNameTrick()));

        colDescription.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getDescription()));

        colDifficulty.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getDifficulty().toLowerCase()));

        colCategory.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getCategory()));

        loadTricks();
    }

    private String getDifficultyStringFromSliderValue(int value) {
        if (value == 0) {
            return "beginner";
        } else if (value == 1) {
            return "intermediate";
        } else if (value == 2) {
            return "advanced";
        } else if (value == 3 || value == 4) {
            return "expert";
        }
        return "beginner";
    }

    public void validaDate(String data) throws WrongFormatException {
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

    public void updateDifficultyLabel(){
        String difficulty =  getDifficultyStringFromSliderValue((int)difficultySlider.getValue());
        difficultyLabel.setText(difficulty);
    }
    public void loadTricks(){
        try {
            List<TrickBean> availableTricksBean = learnTrickController.allAvailableTricksDetailed(windowManager.getAuthBean().getToken());
            trickTable.getItems().clear();
            trickTable.getItems().addAll(availableTricksBean);
        }catch(SessionExpiredException _){
            windowManager.logOut();
        }
    }


    @FXML
    public void goToCompetitionsPage() {
        try {
            windowManager.goToOrganizerCompetitions();
        } catch (IOException e) {
            errorLabel.setText(e.getMessage());
        }
    }

    public String getCategory(){
        RadioButton selectedRadio = (RadioButton) trickGroup.getSelectedToggle();
        if (selectedRadio != null) {
            return selectedRadio.getText();
        } else {
            return "";
        }
    }

    @FXML
    public void createTrick() {
        // Get values from the form fields
        String trickName = trickNameTextField.getText();
        String trickCategory = getCategory();
        String date = dateField.getText();
        String trickDescription = descriptionTextArea.getText();
        String difficulty = getDifficultyStringFromSliderValue((int)difficultySlider.getValue());

        try {
            if (trickName.isEmpty() || trickCategory.isEmpty() || trickDescription.isEmpty()) {
                    throw new EmptyFieldException("Rimpi tutti i campi");
            }

            validaDate(date);
            TrickBean newTrick = new TrickBean(trickName, trickDescription, difficulty, trickCategory, date);

            try {
                learnTrickController.registerTrick(windowManager.getAuthBean().getToken(), newTrick);
                trickNameTextField.clear();
                descriptionTextArea.clear();
                dateField.clear();
                trickGroup.selectToggle(flatRadio);
                loadTricks();
            }catch(SessionExpiredException _){
                windowManager.logOut();
            }
        }catch(EmptyFieldException | WrongFormatException e){
            errorLabel.setText(e.getMessage());
        }
    }

    @FXML
    public void goToSkateboardsPage(){
        try {
            windowManager.goToTricks();
        } catch (IOException e) {
            errorLabel.setText(e.getMessage());
        }
    }


    public void logOut() {
        windowManager.logOut();
    }


    public void goToHomePage() {
        try {
            windowManager.goToOrganizerHomePage();
        } catch (IOException e) {
            errorLabel.setText(e.getMessage());
        }
    }
}
