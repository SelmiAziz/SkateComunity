package viewbasic;

import beans.TrickBean;
import controls.LearnTrickController;
import exceptions.EmptyFieldException;
import exceptions.SessionExpiredException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import utils.WindowManagerBasic;

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
    private final  WindowManagerBasic windowManagerBasic = WindowManagerBasic.getInstance();
    private final DateValidatorFormatter dateValidatorFormatter = new DateValidatorFormatter();


    LearnTrickController learnTrickController = new LearnTrickController();

    public void loadTricks(){
        try {
            List<TrickBean> availableTricksBean = learnTrickController.allAvailableTricksDetailed(windowManagerBasic.getAuthBean().getToken());
            eventListView.getItems().clear();
            for (TrickBean trick : availableTricksBean) {
                String trickDisplay = String.format("<<Nome Trick: %s>>   -   <<Categoria: %s>>   -   <<Difficoltà: %s>>   -   <<Descrizione: %s>>",
                        trick.getNameTrick(), trick.getCategory(), trick.getDifficulty(), trick.getDescription());
                eventListView.getItems().add(trickDisplay);
            }
        }catch(SessionExpiredException _){
            windowManagerBasic.logOut();
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
        List<String> list = Arrays.asList(  "Skateboards", "Competitions", "Log Out");
        ObservableList<String> categories = FXCollections.observableArrayList(list);
        choicePage.setItems(categories);
        choicePage.setValue("Tricks");
    }

    @FXML
    public void goToCompetitionsPage() {
        try{
            windowManagerBasic.goToCustomerCompetitions();
        }catch(IOException e){
            errorLabel.setText(e.getMessage());
        }

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

        try {
            String date = dateValidatorFormatter.formatValidateDate(month, day, year);


            if (trickName.isEmpty() || category.isEmpty() || trickDescription.isEmpty() || date.isEmpty()) {
                throw new EmptyFieldException("Compila i campi correttamente");
            }


            TrickBean newTrick = new TrickBean(trickName, trickDescription, difficulty, category, date);
            try {
                learnTrickController.registerTrick(windowManagerBasic.getAuthBean().getToken(), newTrick);

                categoryChoiceBox.setValue("flat");
                difficultyChoiceBox.setValue("medium");
                trickNameTextField.clear();
                descriptionTextArea.clear();
                loadTricks();
            }catch(SessionExpiredException _){
                windowManagerBasic.logOut();
            }
        }catch(EmptyFieldException e){
            errorLabel.setText(e.getMessage());
        }
    }

    private void populateCategoryChoiceBox() {
        List<String> categoryList = Arrays.asList("Flat", "Grind", "Ramp");
        ObservableList<String> pages = FXCollections.observableArrayList(categoryList);
        categoryChoiceBox.setItems(pages);
        categoryChoiceBox.setValue("flat");
    }

    private void populateDifficultyChoiceBox() {
        List<String> difficultyList  = Arrays.asList("Beginner", "Intermediate" , "Advanced", "Expert");
        ObservableList<String> difficulties = FXCollections.observableArrayList(difficultyList);
        difficultyChoiceBox.setItems(difficulties);
        difficultyChoiceBox.setValue("intermediate");
    }


    @FXML
    public void changePage() {
        String page = choicePage.getValue();
        if (page.equals("Competitions")) {
            try {
                windowManagerBasic.goToOrganizerCompetitions();
            } catch (IOException e) {
                errorLabel.setText(e.getMessage());
            }
        }else if(page.equals("Skateboards")){
            try{
                windowManagerBasic.goToSkateboards();
            }catch (IOException e) {
                errorLabel.setText(e.getMessage());
            }
        }else if(page.equals("Log Out")){
            windowManagerBasic.logOut();

        }
    }

}
