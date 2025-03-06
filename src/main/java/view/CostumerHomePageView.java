package view;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import utils.AccountInfo;
import utils.AccountInfoSessionManager;
import utils.SceneManager;

import java.io.IOException;

public class CostumerHomePageView {
    private Stage stage;
    private final SceneManager sceneManager = SceneManager.getInstance();
    private final AccountInfoSessionManager accountInfoSessionManager = AccountInfoSessionManager.getInstance();


    @FXML private Label usernameLabel;
    @FXML private Label coinsLabel;
    @FXML private Label errorLabel;

    public void initialize(){
        updateAccountInfo();
    }

    @FXML
    public void goToEvents() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/CostumerEventsPageView.fxml"));
        Parent root = null;
        try {
            root = loader.load();
            CostumerEventsPageView userEventsPageView = loader.getController();
            Scene scene = new Scene(root, 1200, 800);
            stage = sceneManager.getStage();
            stage.setResizable(false);
            stage.setScene(scene);
            stage.show();
            Platform.runLater(userEventsPageView::loadEvents);
        } catch (IOException e) {
            System.err.println("Errore di I/O: " + e.getMessage());
            errorLabel.setText(e.getMessage());
        }
    }

    public void updateAccountInfo(){
        AccountInfo accountInfo = accountInfoSessionManager.getAccountInfo();
        coinsLabel.setText(String.valueOf(accountInfo.getCoinsNumber()));
        usernameLabel.setText(accountInfo.getUsername());
    }


    public void logOut()  {
        //SessionManager.getInstance().terminateSession();
        try {
            sceneManager.loadScene("AccessView.fxml");
        }catch (IOException e){
            System.err.println("Errore di I/O: " + e.getMessage());
            errorLabel.setText(e.getMessage());
        }
    }


}
