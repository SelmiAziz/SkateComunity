package view;


import beans.TrickBean;
import controls.LearnTrickController;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import utils.WindowManager;
import utils.SessionManager;

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
    @FXML private DatePicker datePicker;
    @FXML private Label difficultyLabel;
    private Stage stage;

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



    public void updateDifficultyLabel(){
        String difficulty =  getDifficultyStringFromSliderValue((int)difficultySlider.getValue());
        difficultyLabel.setText(difficulty);
    }
    public void loadTricks(){
        List<TrickBean> availableTricksBean = learnTrickController.allAvailableTricksDetailed(windowManager.getAuthBean().getToken());
        trickTable.getItems().clear();
        trickTable.getItems().addAll(availableTricksBean);
    }


    @FXML
    public void goToCompetitionsPage() {





    }
    public String formatFromDataPicker(String date){
        String[] parts = date.split("-");
        return parts[2] + "-" + parts[1] + "-" + parts[0];
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
        String date = formatFromDataPicker( datePicker.getValue().toString());
        String trickDescription = descriptionTextArea.getText();
        String difficulty = getDifficultyStringFromSliderValue((int)difficultySlider.getValue());

        if (trickName.isEmpty() || trickCategory.isEmpty() || trickDescription.isEmpty()) {
            errorLabel.setText("All fields must be filled.");
            return;
        }
        TrickBean newTrick = new TrickBean(trickName, trickDescription, difficulty, trickCategory, date);

        learnTrickController.RegisterTrick(windowManager.getAuthBean().getToken(),newTrick);
        trickNameTextField.clear();
        descriptionTextArea.clear();
        datePicker.setValue(null);
        trickGroup.selectToggle(flatRadio);
        loadTricks();
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
        try {
            windowManager.logOut();
        } catch (IOException e) {
            errorLabel.setText(e.getMessage());
        }
    }

    // Navigate to the home page
    public void goToHomePage() {
        try {
            windowManager.goToOrganizerHomePage();
        } catch (IOException e) {
            errorLabel.setText(e.getMessage());
        }
    }
}
