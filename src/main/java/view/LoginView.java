package view;

import controls.LoginController;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import model.User;
import utils.SceneManager;
import utils.SessionManager;

import javax.imageio.IIOException;
import java.io.IOException;

public class LoginView {
    private final SceneManager sceneManager = SceneManager.getInstance();
    private LoginController loginController = new LoginController();

    @FXML private TextField usernameField;
    @FXML private TextField passwordField;

    @FXML private ToggleGroup toggleGruppo;
    @FXML private ToggleButton btnUser;
    @FXML private ToggleButton btnManager;

    @FXML
    public void goBack() throws IOException {
        sceneManager.loadScene("AccessView.fxml");
    }

    @FXML
    public void goRegistration() throws IOException{
        sceneManager.loadScene("RegisterView.fxml");
    }

    @FXML
    public void submitLogin() throws IOException {
        String username = usernameField.getText();
        String password = passwordField.getText();
        ToggleGroup group = btnUser.getToggleGroup();
        boolean b = false;
        User user = new User(username, password);
        SessionManager sessionManager = SessionManager.getInstance();
        sessionManager.login(user);

        ToggleButton selected = (ToggleButton) btnUser.getToggleGroup().getSelectedToggle();

        if(username.length() < 4 || password.length() < 4){
            System.out.println("Erore nella lunghezza dei vari campi");
        }

        if (username.isEmpty() || password.isEmpty()){
            System.out.println("Errore empty");
            return;
        }

        if(selected == btnUser){
            System.out.println("Hai scelt lo user");
            b = true;
        }else if(selected == btnManager){
            System.out.println("Hai scelto il manager");
            b = false;
        }else{
            System.out.println("Non hai scelto nessuno!!!");
        }


        boolean success = loginController.logUser(username, password);
        if (success){
            if(b){
                sceneManager.loadScene("UserHomePageView.fxml");
            }else{
                sceneManager.loadScene("ManagerHomePageView.fxml");
            }

        }else{
            System.out.println("Non è andata bene");
        }
    }

    public void handleBtnUser(){
        System.out.println("Hello User");
        if (btnUser.isSelected()) {
            // Se btnUser è selezionato, applica lo stile e resetta l'altro
            btnUser.setStyle("-fx-background-color:  #1ABC9C;");
            btnManager.setStyle("-fx-background-color: lightgray;");
        } else {
            // Se deselezionato (anche se, con ToggleGroup, questo avviene quando l'altro viene selezionato)
            btnUser.setStyle("-fx-background-color: lightgray;");
        }
    }

    public void handleBtnManager(){
        System.out.println("Hello manager");
        if (btnManager.isSelected()) {
            // Se btnManager è selezionato, applica lo stile e resetta l'altro
            btnManager.setStyle("-fx-background-color:  #1ABC9C;");
            btnUser.setStyle("-fx-background-color: lightgray;");
        } else {
            btnManager.setStyle("-fx-background-color: lightgray;");
        }
    }
}
