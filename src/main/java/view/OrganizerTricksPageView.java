package view;


import beans.TrickBean;
import controls.LearnTrickController;
import exceptions.DataAccessException;
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
    DateValidator dateValidator = new DateValidator();

    @FXML Label errorLabel;
    @FXML private TableView<TrickBean> trickTable;
    @FXML private TableColumn<TrickBean, String> colTrickName;
    @FXML private TableColumn<TrickBean, String> colDescription;
    @FXML private TableColumn<TrickBean, String> colDifficulty;
    @FXML private TableColumn<TrickBean, String> colCategory;
    @FXML private Slider difficultySlider;
    @FXML private TextField trickNameTextField;
    @FXML private ToggleGroup trickGroup;
    @FXML private RadioButton flatRadio;
    @FXML private RadioButton grindRadio;
    @FXML private RadioButton    rampRadio;
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
        }catch(DataAccessException e){
            errorLabel.setText(e.getMessage());
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
        try {
            TrickBean trick = collectTrickInput();
            registerTrick(trick);
        } catch (EmptyFieldException | WrongFormatException e) {
            errorLabel.setText(e.getMessage());
        }
    }

    private TrickBean collectTrickInput() throws EmptyFieldException, WrongFormatException {
        String trickName = trickNameTextField.getText();
        String trickCategory = getCategory();
        String date = dateField.getText();
        String trickDescription = descriptionTextArea.getText();
        String difficulty = getDifficultyStringFromSliderValue((int)difficultySlider.getValue());

        if (trickName.isEmpty() || trickCategory.isEmpty() || trickDescription.isEmpty()) {
            throw new EmptyFieldException("Rimpi tutti i campi");
        }

        dateValidator.validaDate(date);

        return new TrickBean(trickName, trickDescription, difficulty, trickCategory, date);
    }

    private void registerTrick(TrickBean trick) {
        try {
            learnTrickController.registerTrick(windowManager.getAuthBean().getToken(), trick);
            trickNameTextField.clear();
            descriptionTextArea.clear();
            dateField.clear();
            trickGroup.selectToggle(flatRadio);
            loadTricks();
        } catch (SessionExpiredException _) {
            windowManager.logOut();
        }catch(DataAccessException e){
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
