package viewBasic;

import beans.TrickBean;
import controls.LearnTrickController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import utils.WindowManager;
import utils.WindowManagerBasic;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class CustomerTricksPageViewBasic {
    @FXML Label errorLabel;
    @FXML private TextField trickNameTextField;
    @FXML private Label descriptionLabel;
    @FXML private ListView<String> eventListView;
    @FXML private Label difficultyLabel;
    @FXML private Label categoryLabel;
    @FXML private ChoiceBox<String> choicePage;


    WindowManagerBasic windowManagerBasic = WindowManagerBasic.getInstance();
    LearnTrickController learnTrickController = new LearnTrickController();

    public void loadTricks(){
        List<TrickBean> availableTricksBean = learnTrickController.allAvailableTricks(windowManagerBasic.getAuthBean().getToken());
        eventListView.getItems().clear();
        for (TrickBean trick : availableTricksBean) {
            String trickDisplay = String.format("<<Nome Trick: %s>>",
                    trick.getNameTrick());
            eventListView.getItems().add(trickDisplay);
        }
    }


    private void populatePageChoice() {
        List<String> list = Arrays.asList( "Board", "Competitions", "Log Out");
        ObservableList<String> categories = FXCollections.observableArrayList(list);
        choicePage.setItems(categories);
        choicePage.setValue("Learn");
    }



    @FXML
    public void initialize() {
        loadTricks();
        populatePageChoice();
    }



    public void showTrick(){
        String trickName = trickNameTextField.getText();
        TrickBean trickBean = new TrickBean(trickName);
        TrickBean detailedTrick = learnTrickController.detailsTrick(windowManagerBasic.getAuthBean().getToken(),trickBean);
        descriptionLabel.setText("Description: " + detailedTrick.getDescription());
        categoryLabel.setText("Category: " +detailedTrick.getCategory());
        difficultyLabel.setText("Difficulty: " +detailedTrick.getDifficulty().toLowerCase());
    }






    @FXML
    public void changePage(){
        String page = choicePage.getValue();
        if(page.equals("Competitions")){
            try {
                windowManagerBasic.goToCustomerCompetitions();
            } catch(IOException e){
                errorLabel.setText(e.getMessage());
            }
        }else if(page.equals("Board")){
                windowManagerBasic.goToOrderPage();
        }else if(page.equals("Log Out")){
            try {
                windowManagerBasic.logOut();
            } catch(IOException e){
                errorLabel.setText(e.getMessage());
            }
        }
    }

    @FXML
    public void goToCompetitionsPage(){

    }

}
