package viewbasic;

import beans.BoardProfileBean;
import controls.CreateBoardController;
import exceptions.DataAccessException;
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

public class OrganizerSkateboardsPageViewBasic {
    @FXML private Label errorLabel;
    @FXML private TextField costTextField;
    @FXML private ChoiceBox<String> sizeChoiceBox;
    @FXML private TextField skateboardNameField;
    @FXML private TextArea descriptionTextArea;


    @FXML private ListView<String> skateboardList;
    @FXML private ChoiceBox<String> choicePage;

    WindowManagerBasic windowManagerBasic = WindowManagerBasic.getInstance();
    CreateBoardController createSkateboardController = new CreateBoardController();

    public void initialize(){
        populateChoiceBox();
        loadSkateboards();
        populatePageChoice();

    }

    private void populatePageChoice() {
        List<String> list = Arrays.asList( "Competitions", "Tricks", "Log Out");
        ObservableList<String> pages = FXCollections.observableArrayList(list);
        choicePage.setItems(pages);
        choicePage.setValue("Skateboards");
    }

    public void loadSkateboards() {
        try {
            List<BoardProfileBean> skateboards = createSkateboardController.getStoredBoards(windowManagerBasic.getAuthBean().getToken());
            skateboardList.getItems().clear();
            for (BoardProfileBean bean : skateboards) {
                String display = String.format(
                        "<<Nome: %s>>-<<Description: %s>>-<<Size: %s>>-<<Cost: %d>>",
                        bean.getName(),
                        bean.getDescription(),
                        bean.getSize(),
                        bean.getPrice()
                );
                skateboardList.getItems().add(display);
            }
        }catch(SessionExpiredException _){
            windowManagerBasic.logOut();
        }catch(DataAccessException e){
            errorLabel.setText(e.getMessage());
        }
    }



    private void populateChoiceBox() {
        String[] dim = {"7.0", "7.25", "7.5", "7.75", "8.0", "8.25", "8.5"};
        sizeChoiceBox.setItems(FXCollections.observableArrayList(dim));
        sizeChoiceBox.setValue(dim[0]);
    }




    public void submitSkateboard(){
        String size = sizeChoiceBox.getValue();
        String name = skateboardNameField.getText();
        String description = descriptionTextArea.getText();

        try {
            if(name == null || description == null){
                throw new EmptyFieldException("Inserire correttamente campi");
            }
            int cost = Integer.parseInt(costTextField.getText());
            createSkateboard(name, description, size, cost);

        } catch(NumberFormatException | EmptyFieldException e) {
            errorLabel.setText(e.getMessage());
        }
    }

    private void createSkateboard(String name, String description, String size, int cost) {
        try {
            createSkateboardController.createBoard(
                    windowManagerBasic.getAuthBean().getToken(),
                    new BoardProfileBean(name, description, size, cost));
            loadSkateboards();
        } catch (SessionExpiredException _) {
            windowManagerBasic.logOut();
        }catch(DataAccessException e){
            errorLabel.setText(e.getMessage());
        }
    }



    @FXML
    public void changePage(){
        String page = choicePage.getValue();
        if(page.equals("Tricks")){
            try {
                windowManagerBasic.goToTricks();
            } catch(IOException e){
                errorLabel.setText(e.getMessage());
            }
        }else if(page.equals("Competitions")){
            try {
                windowManagerBasic.goToOrganizerCompetitions();
            } catch(IOException e){
                errorLabel.setText(e.getMessage());
            }
        }else if(page.equals("Log Out")){
            windowManagerBasic.logOut();
        }
    }

}