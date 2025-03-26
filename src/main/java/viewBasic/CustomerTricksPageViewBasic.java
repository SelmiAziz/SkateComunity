package viewBasic;

import beans.TrickBean;
import controls.LearnTrickController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import utils.SceneManager;
import utils.SessionManager;

import java.io.IOException;
import java.util.List;

public class CustomerTricksPageViewBasic {
    @FXML Label errorLabel;
    @FXML private TextField trickNameTextField;
    @FXML private Label descriptionLabel;
    @FXML private ListView<String> eventListView;
    @FXML private Label difficultyLabel;
    @FXML private Label categoryLabel;
    private Stage stage;

    LearnTrickController learnTrickController = new LearnTrickController();

    public void loadTricks(){
        List<TrickBean> availableTricksBean = learnTrickController.allAvailableTricks();
        eventListView.getItems().clear();
        for (TrickBean trick : availableTricksBean) {
            String trickDisplay = String.format("<<Nome Trick: %s>>",
                    trick.getNameTrick());
            eventListView.getItems().add(trickDisplay);
        }
    }


    @FXML
    public void initialize() {
        loadTricks();
    }

    private String formatDifficultyForView(String difficulty){
        return difficulty.toLowerCase();
    }

    private String formatDifficultyForControl(String difficulty){
        return difficulty.toUpperCase();
    }


    public void showTrick(){
        String trickName = trickNameTextField.getText();
        TrickBean trickBean = new TrickBean(trickName);
        TrickBean detailedTrick = learnTrickController.detailsTrick(trickBean);
        descriptionLabel.setText("Description: " + detailedTrick.getDescription());
        categoryLabel.setText("Category: " +detailedTrick.getCategory());
        difficultyLabel.setText("Difficulty: " +detailedTrick.getDifficulty().toLowerCase());
    }




    @FXML
    public void logOut() {
        SessionManager.getInstance().terminateSession();
        try {
            SceneManager.getInstance().loadScene("viewFxmlBasic/AccessViewBasic.fxml");
        } catch (IOException e) {
            errorLabel.setText(e.getMessage());
        }
    }

    @FXML
    public void goToEvents() {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/viewFxmlBasic/CustomerEventsPageViewBasic.fxml"));
        Parent root = null;
        try {
            root = loader.load();
            CustomerEventsPageViewBasic customerEventsPageViewBasic = loader.getController();
            Scene scene = new Scene(root, 1200, 800);
            stage = SceneManager.getInstance().getStage();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
            //Platform.runLater(customerEventsPageViewBasic::loadEvents);
        } catch (IOException e) {
            errorLabel.setText(e.getMessage());
        }

    }

    @FXML
    public void goToCompetitionsPage(){

    }

    @FXML
    public void goToHomePage() {
        try {
            SceneManager.getInstance().loadScene("viewFxmlBasic/CustomerHomePageViewBasic.fxml");
        } catch (IOException e) {
            errorLabel.setText(e.getMessage());
        }
    }
}
