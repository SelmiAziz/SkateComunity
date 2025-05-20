package view;

import beans.BoardProfileBean;
import controls.CreateBoardController;
import exceptions.EmptyFieldException;
import exceptions.SessionExpiredException;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import utils.WindowManager;

import java.io.IOException;
import java.util.List;

public class OrganizerSkateboardsPageView {
    @FXML private Label errorLabel;
    @FXML private Spinner<Integer> costSpinner;
    @FXML private Spinner<String> sizeSpinner;
    @FXML private TextField skateboardNameField;
    @FXML private TextArea descriptionTextArea;
    @FXML private TableView<BoardProfileBean> skateboardTable;
    @FXML private TableColumn<BoardProfileBean, String> colSkateboardName;
    @FXML private TableColumn<BoardProfileBean, String> colDescription;
    @FXML private TableColumn<BoardProfileBean, String> colSize;
    @FXML private TableColumn<BoardProfileBean, String> colCost;
    WindowManager windowManager = WindowManager.getInstance();
    CreateBoardController createSkateboardController = new CreateBoardController();

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
        try {
            List<BoardProfileBean> availableSkateboardsList = createSkateboardController.getStoredBoards(windowManager.getAuthBean().getToken());
            skateboardTable.getItems().clear();
            skateboardTable.getItems().addAll(availableSkateboardsList);
        }catch(SessionExpiredException _){
            windowManager.logOut();
        }
    }



    public void submitSkateboard() {
        String size = sizeSpinner.getValue();
        int cost = costSpinner.getValue();
        String name = skateboardNameField.getText();
        String description = descriptionTextArea.getText();

        try {
            if (name == null || description == null) {
                throw new EmptyFieldException("Inserire correttamente campi");
            }
            createBoard(name, description, size, cost);
        } catch (EmptyFieldException e) {
            errorLabel.setText(e.getMessage());
        }
    }

    private void createBoard(String name, String description, String size, int cost) {
        try {
            createSkateboardController.createBoard(
                    windowManager.getAuthBean().getToken(),
                    new BoardProfileBean(name, description, size, cost)
            );
            loadSkateboards();
        } catch (SessionExpiredException _) {
            windowManager.logOut();
        }
    }



    @FXML
    public void goToTricksPage(){
        try {
           windowManager.goToTricks();
        }catch(IOException e){
            errorLabel.setText(e.getMessage());
        }
    }

    @FXML
    public void goToCompetitionsPage(){
        try {
            windowManager.goToOrganizerCompetitions();
        } catch (IOException e) {
            errorLabel.setText(e.getMessage());
        }
    }



    public void goToHomePage() {
        try {
            windowManager.goToOrganizerHomePage();
        } catch (IOException e) {
            errorLabel.setText(e.getMessage());
        }
    }



    public void logOut() {
        windowManager.logOut();

    }

}
