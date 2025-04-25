package viewBasic;

import beans.CustomizedBoardBean;
import controls.CreateBoardController;
import exceptions.EmptyFieldException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import utils.SceneManager;

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

    SceneManager sceneManager = SceneManager.getInstance();
    CreateBoardController createSkateboardController = new CreateBoardController();

    public void initialize(){
        populateChoiceBox();
        loadSkateboards();
        populatePageChoice();

    }

    private void populatePageChoice() {
        List<String> list = Arrays.asList( "Competitions", "Tricks", "Log Out");
        ObservableList<String> categories = FXCollections.observableArrayList(list);
        choicePage.setItems(categories);
        choicePage.setValue("Skateboards");
    }

    public void loadSkateboards() {
        List<CustomizedBoardBean> skateboards = createSkateboardController.getStoredBoards();
        skateboardList.getItems().clear();
        for (CustomizedBoardBean bean : skateboards) {
            String display = String.format(
                    "<<Nome: %s>>-<<Description: %s>>-<<Size: %s>>-<<Cost: %d>>",
                    bean.getName(),
                    bean.getDescription(),
                    bean.getSize(),
                    bean.getPrice()
            );
            skateboardList.getItems().add(display);
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
        try{
            if(name == null || description == null){
                throw new EmptyFieldException("Inserire correttamente campi");
            }
            int cost = Integer.parseInt(costTextField.getText());
            createSkateboardController.createBoard( new CustomizedBoardBean(name, description, size, cost));
            loadSkateboards();
        }catch(NumberFormatException | EmptyFieldException e){
            errorLabel.setText(e.getMessage());
        }

    }


    @FXML
    public void changePage(){
        String page = choicePage.getValue();
        if(page.equals("Tricks")){
            try {
                sceneManager.loadScene("viewFxmlBasic/OrganizerTricksPageViewBasic.fxml");
            } catch(IOException e){
                errorLabel.setText(e.getMessage());
            }
        }else if(page.equals("Competitions")){
            try {
                sceneManager.loadScene("viewFxmlBasic/OrganizerCompetitionsPageViewBasic.fxml");
            } catch(IOException e){
                errorLabel.setText(e.getMessage());
            }
        }else if(page.equals("Log Out")){
            try {
                sceneManager.loadScene("viewFxmlBasic/LogPageBasicView.fxml");
            } catch(IOException e){
                errorLabel.setText(e.getMessage());
            }
        }
    }

}