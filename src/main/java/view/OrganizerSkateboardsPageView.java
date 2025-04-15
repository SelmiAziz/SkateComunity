package view;

import beans.SkateboardBean;
import controls.CreateSkateboardController;
import dao.SkateboardDao;
import exceptions.EmptyFieldException;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import utils.SceneManager;
import utils.SessionManager;

import java.io.IOException;
import java.util.List;

public class OrganizerSkateboardsPageView {
    @FXML private Label errorLabel;
    @FXML private Spinner<Integer> costSpinner;
    @FXML private Spinner<String> sizeSpinner;
    @FXML private TextField skateboardNameField;
    @FXML private TextArea descriptionTextArea;
    @FXML private TableView<SkateboardBean> skateboardTable;
    @FXML private TableColumn<SkateboardBean, String> colSkateboardName;
    @FXML private TableColumn<SkateboardBean, String> colDescription;
    @FXML private TableColumn<SkateboardBean, String> colSize;
    @FXML private TableColumn<SkateboardBean, String> colCost;
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

        loadSkateboards();
        String[] dim = {"7.0", "7.25", "7.5", "7.75", "8.0", "8.25", "8.5"};
        sizeSpinner.setValueFactory(new SpinnerValueFactory.ListSpinnerValueFactory<>(
                javafx.collections.FXCollections.observableArrayList(dim)
        ));
        costSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1000, 50));
    }



    public void loadSkateboards(){
        List<SkateboardBean> availableSkateboardsList = createSkateboardController.getStoredSkateboards();
        skateboardTable.getItems().clear();
        for(SkateboardBean s: availableSkateboardsList){
            System.out.println(s.getName());
        }
        skateboardTable.getItems().addAll(availableSkateboardsList);
    }


    public void submitSkateboard(){
      String size = sizeSpinner.getValue();
      int cost = costSpinner.getValue();
      String name = skateboardNameField.getText();
      String description = descriptionTextArea.getText();
      try{
          if(name == null || description == null){
              throw new EmptyFieldException("Inserire correttamente campi");
          }
          createSkateboardController.createSkateboard( new SkateboardBean(name, description, size, cost));
          loadSkateboards();
      }catch(EmptyFieldException e){
          errorLabel.setText(e.getMessage());
      }

    }


    @FXML
    public void goToTricksPage(){
        try {
            SceneManager.getInstance().loadScene("viewFxml/OrganizerTricksPageView.fxml");
        }catch(IOException e){
            errorLabel.setText(e.getMessage());
        }
    }

    @FXML
    public void goToCompetitionsPage(){
        SessionManager.getInstance().terminateSession();
        try {
            SceneManager.getInstance().loadScene("viewFxml/OrganizerCompetitionsPageView.fxml");
        } catch (IOException e) {
            errorLabel.setText(e.getMessage());
        }
    }



    public void goToHomePage() {
        try {
            SceneManager.getInstance().loadScene("viewFxml/OrganizerHomePageView.fxml");
        } catch (IOException e) {
            errorLabel.setText(e.getMessage());
        }
    }



    public void logOut()  {
        //here you have to call the controller to logOut
        try {
            sceneManager.loadScene("viewFxml/AccessView.fxml");
        }catch(IOException e){
            errorLabel.setText(e.getMessage());
        }
    }

}
