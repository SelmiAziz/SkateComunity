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

public class CustomerTricksPageView {

    private WindowManager windowManager = WindowManager.getInstance();
    private Stage stage;
    @FXML Label errorLabel;
    @FXML private TableView<TrickBean> trickTable;
    @FXML private TableColumn<TrickBean, String> colTrickName;


    @FXML private TextField trickNameTextField;
    @FXML private Label categoryLabel;
    @FXML private Label difficultyLabel;
    @FXML private Label descriptionLabel;

    LearnTrickController learnTrickController = new LearnTrickController();

    @FXML
    public void initialize() {

        colTrickName.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getNameTrick()));

        loadTricks();
    }

    public void selectTrick(){
        TrickBean trickBean = trickTable.getSelectionModel().getSelectedItem();
        showTrick(trickBean);
    }

    @FXML
    public void goToCompetitionsPage() {
        try {
            windowManager.goToCustomerCompetitions();
        } catch (IOException e) {
            //errorLabel.setText(e.getMessage());
        }
    }


    @FXML
    public void goToCommissionsPage(){

    }


    public void showTrick(TrickBean trickBean){
        TrickBean detailedTrick = learnTrickController.detailsTrick(trickBean);
        descriptionLabel.setText("Description: " + detailedTrick.getDescription());
        categoryLabel.setText("Category: " +detailedTrick.getCategory());
        difficultyLabel.setText("Difficulty: " +detailedTrick.getDifficulty().toLowerCase());
    }


    public void loadTricks(){
        List<TrickBean> availableTricksBean = learnTrickController.allAvailableTricksDetailed();
        trickTable.getItems().clear();
        trickTable.getItems().addAll(availableTricksBean);
    }


    public void logOut() {
        try {
            windowManager.logOut();
        }catch(IOException e){
            errorLabel.setText(e.getMessage());
        }
    }

    // Navigate to the home page
    public void goToHomePage() {
        try {
            windowManager.goToHomePage();
        } catch (IOException e) {
            errorLabel.setText(e.getMessage());
        }
    }
}
