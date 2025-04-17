package viewBasic;

import beans.SkateboardBean;
import controls.CreateSkateboardController;
import exceptions.EmptyFieldException;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import utils.SceneManager;

import java.io.IOException;
import java.util.List;

public class OrganizerSkateboardsPageViewBasic {
    @FXML private Label errorLabel;
    @FXML private TextField costTextField;
    @FXML private ChoiceBox<String> sizeChoiceBox;
    @FXML private TextField skateboardNameField;
    @FXML private TextArea descriptionTextArea;
    @FXML private TableView<SkateboardBean> skateboardTable;
    @FXML private TableColumn<SkateboardBean, String> colSkateboardName;
    @FXML private TableColumn<SkateboardBean, String> colDescription;
    @FXML private TableColumn<SkateboardBean, String> colSize;
    @FXML private TableColumn<SkateboardBean, String> colCost;
    @FXML private ChoiceBox<String> choicePage;

    SceneManager sceneManager = SceneManager.getInstance();
    CreateSkateboardController createSkateboardController = new CreateSkateboardController();

    public void initialize(){
        colSkateboardName.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getName()));

        colDescription.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getDescription()));

        colSize.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getSize()));

        colCost.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getPrice())));

        populateChoiceBox();
        loadSkateboards();

    }



    private void populateChoiceBox() {
        String[] dim = {"7.0", "7.25", "7.5", "7.75", "8.0", "8.25", "8.5"};
        sizeChoiceBox.setItems(FXCollections.observableArrayList(dim));
        sizeChoiceBox.setValue(dim[0]);
    }

    public void loadSkateboards(){
        List<SkateboardBean> availableSkateboardsList = createSkateboardController.getStoredSkateboards();
        skateboardTable.getItems().clear();
        skateboardTable.getItems().addAll(availableSkateboardsList);
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
            createSkateboardController.createSkateboard( new SkateboardBean(name, description, size, cost));
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