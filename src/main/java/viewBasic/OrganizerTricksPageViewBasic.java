package viewBasic;

import beans.TrickBean;
import controls.LearnTrickController;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import utils.SceneManager;
import utils.SessionManager;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class OrganizerTricksPageViewBasic {
    @FXML Label errorLabel;
    @FXML private TextField trickNameTextField;
    @FXML private TextArea descriptionTextArea;
    @FXML private ListView<String> eventListView;
    @FXML private ChoiceBox<String> categoryChoiceBox;
    @FXML private ChoiceBox<String> difficultyChoiceBox;
    @FXML private TextField monthField;
    @FXML private TextField dayField;
    @FXML private TextField yearField;
    private Stage stage;

    LearnTrickController learnTrickController = new LearnTrickController();

    public void loadTricks(){
        List<TrickBean> availableTricksBean = learnTrickController.allAvailableTricksDetailed();
        eventListView.getItems().clear();
        for (TrickBean trick : availableTricksBean) {
            String trickDisplay = String.format("<<Nome Trick: %s>>   -   <<Categoria: %s>>   -   <<Difficoltà: %s>>   -   <<Descrizione: %s>>",
                    trick.getNameTrick(), trick.getCategory(), formatDifficultyForView(trick.getDifficulty()), trick.getDescription());
            eventListView.getItems().add(trickDisplay);
        }
    }


    @FXML
    public void initialize() {
      loadTricks();
      populateCategoryChoiceBox();
      populateDifficultyChoiceBox();
    }

    private String formatDifficultyForView(String difficulty){
        return difficulty.toLowerCase();
    }

    private String formatDifficultyForControl(String difficulty){
        return difficulty.toUpperCase();
    }

    String formatDate(String month, String day, String year){
        return day+"-"+month+"-20"+year;
    }


    @FXML
    public void goToEvents() {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/viewFxmlBasic/OrganizerEventsPageViewBasic.fxml"));
        Parent root = null;
        try {
            root = loader.load();
            OrganizerEventsPageViewBasic organizerEventsPageViewBasic = loader.getController();
            Scene scene = new Scene(root, 1200, 800);
            stage = SceneManager.getInstance().getStage();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
            Platform.runLater(organizerEventsPageViewBasic::loadEvents);
        } catch (IOException e) {
            errorLabel.setText(e.getMessage());
        }

    }

    @FXML
    public void createTrick() {
        String trickName = trickNameTextField.getText();
        String category = categoryChoiceBox.getValue();
        String trickDescription = descriptionTextArea.getText();
        String difficulty =  formatDifficultyForControl(difficultyChoiceBox.getValue());
        String month = monthField.getText();
        String day = dayField.getText();
        String year = yearField.getText();

        String date = formatDate(month,day,year);


        if (trickName.isEmpty() || category.isEmpty() || trickDescription.isEmpty() || date.isEmpty()) {
            errorLabel.setText("All fields must be filled.");
            return;
        }


        TrickBean newTrick = new TrickBean(trickName, trickDescription, difficulty, category, date);

        learnTrickController.RegisterTrick(newTrick);

        categoryChoiceBox.setValue("flat");
        difficultyChoiceBox.setValue("medium");
        trickNameTextField.clear();
        descriptionTextArea.clear();
        loadTricks();
    }

    private void populateCategoryChoiceBox() {
        List<String> categoryList = Arrays.asList("flat", "grind", "ramp");
        ObservableList<String> categories = FXCollections.observableArrayList(categoryList);
        categoryChoiceBox.setItems(categories);
        categoryChoiceBox.setValue("flat");
    }

    private void populateDifficultyChoiceBox() {
        List<String> difficultyList  = Arrays.asList("beginner", "intermediate" , "advanced", "expert");
        ObservableList<String> difficulties = FXCollections.observableArrayList(difficultyList);
        difficultyChoiceBox.setItems(difficulties);
        difficultyChoiceBox.setValue("intermediate");
    }


    public void logOut() {
        SessionManager.getInstance().terminateSession();
        try {
            SceneManager.getInstance().loadScene("viewFxml/AccessView.fxml");
        } catch (IOException e) {
            errorLabel.setText(e.getMessage());
        }
    }
    @FXML
    public void goToCompetitionsPage(){

    }

    public void goToHomePage() {
        try {
            SceneManager.getInstance().loadScene("viewFxml/OrganizerHomePageView.fxml");
        } catch (IOException e) {
            errorLabel.setText(e.getMessage());
        }
    }
}
