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
import utils.SceneManager;
import utils.SessionManager;
import viewBasic.CustomerEventsPageViewBasic;

import java.io.IOException;
import java.util.List;

public class CustomerTricksPageView {

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

    @FXML
    public void goToEvents() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/viewFxml/CustomerEventsPageView.fxml"));
        Parent root = null;
        try {
            root = loader.load();
            CustomerEventsPageView userEventsPageView = loader.getController();
            Scene scene = new Scene(root, 1200, 800);
            stage =SceneManager.getInstance().getStage();
            stage.setResizable(false);
            stage.setScene(scene);
            stage.show();
            Platform.runLater(userEventsPageView::loadEvents);
        } catch (IOException e) {
            errorLabel.setText(e.getMessage());
        }
    }



    public void showTrick(){
        String trickName = trickNameTextField.getText();
        TrickBean trickBean = new TrickBean(trickName);
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


    // Log out and navigate to the access view
    public void logOut() {
        SessionManager.getInstance().terminateSession();
        try {
            SceneManager.getInstance().loadScene("viewFxml/AccessView.fxml");
        } catch (IOException e) {
            errorLabel.setText(e.getMessage());
        }
    }

    // Navigate to the home page
    public void goToHomePage() {
        try {
            SceneManager.getInstance().loadScene("viewFxml/OrganizerHomePageView.fxml");
        } catch (IOException e) {
            errorLabel.setText(e.getMessage());
        }
    }
}
