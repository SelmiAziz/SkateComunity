package viewBasic;

import beans.TrickBean;
import controls.LearnTrickController;
import exceptions.WrongFormatException;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import utils.WindowManager;

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
    @FXML private ChoiceBox<String> choicePage;
    private Stage stage;
    WindowManager sceneManager = WindowManager.getInstance();


    LearnTrickController learnTrickController = new LearnTrickController();

    public void loadTricks(){
        List<TrickBean> availableTricksBean = learnTrickController.allAvailableTricksDetailed("");
        eventListView.getItems().clear();
        for (TrickBean trick : availableTricksBean) {
            String trickDisplay = String.format("<<Nome Trick: %s>>   -   <<Categoria: %s>>   -   <<Difficoltà: %s>>   -   <<Descrizione: %s>>",
                    trick.getNameTrick(), trick.getCategory(), trick.getDifficulty(), trick.getDescription());
            eventListView.getItems().add(trickDisplay);
        }
    }


    @FXML
    public void initialize() {
      loadTricks();
      populateCategoryChoiceBox();
      populateDifficultyChoiceBox();
      populatePageChoice();
    }


    private void populatePageChoice() {
        List<String> list = Arrays.asList( "Commissions", "Competitions", "Log Out");
        ObservableList<String> categories = FXCollections.observableArrayList(list);
        choicePage.setItems(categories);
        choicePage.setValue("Commissions");
    }

    @FXML
    public void goToCompetitionsPage() {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/viewFxmlBasic/OrganizerCompetitionsPageViewBasic.fxml"));
        Parent root = null;
        try {
            root = loader.load();
            OrganizerCompetitionsPageViewBasic organizerEventsPageViewBasic = loader.getController();
            Scene scene = new Scene(root, 1200, 800);
            stage = WindowManager.getInstance().getStage();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
            Platform.runLater(organizerEventsPageViewBasic::loadCompetitions);
        } catch (IOException e) {
            errorLabel.setText(e.getMessage());
        }

    }

    private String formatValidateDate(String month, String day, String year) throws WrongFormatException {
        int monthInt = Integer.parseInt(month);
        if (monthInt < 1 || monthInt > 12) {
            throw new WrongFormatException("Mese non valido: " + month);
        }

        int dayInt = Integer.parseInt(day);
        int[] daysInMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

        int yearInt = Integer.parseInt(year);
        if (monthInt == 2 && ((yearInt % 4 == 0 && yearInt % 100 != 0) || (yearInt % 400 == 0))) {
            daysInMonth[1] = 29;
        }

        if (dayInt < 1 || dayInt > daysInMonth[monthInt - 1]) {
            throw new WrongFormatException("Giorno non valido: " + day + " per il mese " + month);
        }

        return day + "/" + month + "/20" + year;
    }

    @FXML
    public void createTrick() {
        String trickName = trickNameTextField.getText();
        String category = categoryChoiceBox.getValue();
        String trickDescription = descriptionTextArea.getText();
        String difficulty =  difficultyChoiceBox.getValue();
        String month = monthField.getText();
        String day = dayField.getText();
        String year = yearField.getText();

        String date = formatValidateDate(month,day,year);


        if (trickName.isEmpty() || category.isEmpty() || trickDescription.isEmpty() || date.isEmpty()) {
            errorLabel.setText("All fields must be filled.");
            return;
        }


        TrickBean newTrick = new TrickBean(trickName, trickDescription, difficulty, category, date);

        learnTrickController.RegisterTrick("",newTrick);

        categoryChoiceBox.setValue("flat");
        difficultyChoiceBox.setValue("medium");
        trickNameTextField.clear();
        descriptionTextArea.clear();
        loadTricks();
    }

    private void populateCategoryChoiceBox() {
        List<String> categoryList = Arrays.asList("Flat", "Grind", "Ramp");
        ObservableList<String> categories = FXCollections.observableArrayList(categoryList);
        categoryChoiceBox.setItems(categories);
        categoryChoiceBox.setValue("flat");
    }

    private void populateDifficultyChoiceBox() {
        List<String> difficultyList  = Arrays.asList("Beginner", "Intermediate" , "Advanced", "Expert");
        ObservableList<String> difficulties = FXCollections.observableArrayList(difficultyList);
        difficultyChoiceBox.setItems(difficulties);
        difficultyChoiceBox.setValue("intermediate");
    }


    @FXML
    public void changePage(){
        String page = choicePage.getValue();
        if(page.equals("Competitions")){
            try {
                sceneManager.loadScene("viewFxmlBasic/OrganizerCompetitionsPageViewBasic.fxml");
            } catch(IOException e){
                errorLabel.setText(e.getMessage());
            }
        }else if(page.equals("Commissions")){
            try {
                sceneManager.loadScene("viewFxmlBasic/LogPageBasicViewBasic.fxml");
            } catch(IOException e){
                errorLabel.setText(e.getMessage());
            }
        }else if(page.equals("Log Out")){
            try {
                sceneManager.loadScene("viewFxmlBasic/LogPageBasicViewBasic.fxml");
            } catch(IOException e){
                errorLabel.setText(e.getMessage());
            }
        }
    }

}
