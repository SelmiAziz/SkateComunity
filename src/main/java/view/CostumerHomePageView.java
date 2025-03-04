package view;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import utils.AccountInfoSession;
import utils.SceneManager;
import utils.SessionManager;

import java.io.IOException;

public class CostumerHomePageView {
    private Stage stage;
    private final SceneManager sceneManager = SceneManager.getInstance();

    @FXML
    private Label coinsLabel;

    @FXML
    public void goToEvents() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/CostumerEventsPageView.fxml"));
        Parent root = loader.load();
        CostumerEventsPageView userEventsPageView = loader.getController();

        Scene scene = new Scene(root, 1200, 800);
        stage = sceneManager.getStage();
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();

        Platform.runLater(userEventsPageView::loadEvents);
    }


    public void updateCoinsInWindow(){
        this.coinsLabel.setText(""+AccountInfoSession.getInstance().getAccountInfo().getCoinsNumber());
    }

    public void logOut()  {
        //SessionManager.getInstance().terminateSession();
        try {
            sceneManager.loadScene("AccessView.fxml");
        }catch (IOException e){

        }
    }


}
