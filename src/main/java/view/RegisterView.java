package view;

import controls.RegisterController;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import utils.SceneManager;

import java.io.IOException;

public class RegisterView {
    private final SceneManager sceneManager = SceneManager.getInstance();


    @FXML private TextField usernameField;
    @FXML private TextField passwordField;
    @FXML private TextField passwordFieldConferma;

    private final RegisterController registerController = new RegisterController();
    @FXML private ToggleGroup toggleGruppo;
    @FXML private ToggleButton btnUser;
    @FXML private ToggleButton btnManager;

    @FXML
    public void submitRegistration(){
        String username = usernameField.getText();
        String password = passwordField.getText();
        String passwordConferma = passwordFieldConferma.getText();
        ToggleGroup group = btnUser.getToggleGroup();

        ToggleButton selected = (ToggleButton) btnUser.getToggleGroup().getSelectedToggle();

        if(username.length() < 4 || password.length() < 4 || passwordConferma.length() < 4){
            System.out.println("Erore nella lunghezza dei vari campi");
        }

        if (username.isEmpty() || password.isEmpty()){
            System.out.println("Errore empty");
            return;
        }

        if(selected == btnUser){
            System.out.println("Hai scelt lo user");
        }else if(selected == btnManager){
            System.out.println("Hai scelto il manager");
        }else{
            System.out.println("Non hai scelto nessuno!!!");
        }

        System.out.println(passwordConferma);
        System.out.println(password);
        if(!password.equals(passwordConferma)){
            System.out.println("Errore nella conferma password");
            return;
        }
        boolean success = registerController.registerUser(username, password);
        if (success){
            System.out.println("È andata bene ");
        }else{
            System.out.println("Non è andata bene");
        }
    }

    @FXML
    public void handleBtnUser() {
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

    @FXML
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



    @FXML
    public void goBack() throws IOException {
        sceneManager.loadScene("AccessView.fxml");
    }

    @FXML
    public void goLogin() throws  IOException{
        sceneManager.loadScene("LoginView.fxml");
    }
}
