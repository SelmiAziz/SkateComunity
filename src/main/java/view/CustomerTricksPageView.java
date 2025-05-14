package view;


import beans.TrickBean;
import controls.LearnTrickController;
import exceptions.SessionExpiredException;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import utils.WindowManager;

import java.io.IOException;
import java.util.List;

public class CustomerTricksPageView {

    private WindowManager windowManager = WindowManager.getInstance();
    @FXML Label errorLabel;
    @FXML private TableView<TrickBean> trickTable;
    @FXML private TableColumn<TrickBean, String> colTrickName;


    @FXML private Label categoryLabel;
    @FXML private Label difficultyLabel;
    @FXML private Label descriptionLabel;

    LearnTrickController learnTrickController = new LearnTrickController();

    @FXML
    public void initialize() throws IOException {

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
            errorLabel.setText(e.getMessage());
        }
    }


    @FXML
    public void goToOrdersPage(){
        try {
            windowManager.goToOrdersPage();
        } catch (IOException e) {
            errorLabel.setText(e.getMessage());
        }
    }


    public void showTrick(TrickBean trickBean){
        TrickBean detailedTrick = learnTrickController.detailsTrick(windowManager.getAuthBean().getToken(),trickBean);
        descriptionLabel.setText("Description: " + detailedTrick.getDescription());
        categoryLabel.setText("Category: " +detailedTrick.getCategory());
        difficultyLabel.setText("Difficulty: " +detailedTrick.getDifficulty().toLowerCase());
    }


    public void loadTricks()  {
        try {
            List<TrickBean> availableTricksBean = learnTrickController.allAvailableTricksDetailed(windowManager.getAuthBean().getToken());
            trickTable.getItems().clear();
            trickTable.getItems().addAll(availableTricksBean);
        }catch(SessionExpiredException _){
            windowManager.logOut();
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
}
