package view;

import beans.CustomBoardBean;
import beans.BoardBean;
import controls.CustomOrderController;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import utils.SceneManager;

import java.io.IOException;
import java.util.List;

public class CustomerOrdersPageView {

    @FXML private Pane customPane;
    @FXML private Label boardPriceLabel;
    @FXML private TextArea descriptionArea;
    @FXML private Label customizeLabel;
    @FXML private Slider gripSlider;
    @FXML private Label errorLabel;
    @FXML private Spinner<Integer> warrantySpinner;
    @FXML private Spinner<Integer> pilesSpinner;
    @FXML private Spinner<Double> noseSpinner;
    @FXML private Spinner<Double> tailSpinner;
    @FXML private Label gripValueLabel;
    @FXML private TableView<BoardBean> boardTable;
    @FXML private TableColumn<BoardBean, String> colBoardName;
    @FXML private TableColumn<BoardBean, String> colDescription;
    @FXML private TableColumn<BoardBean, String> colSize;
    @FXML private TableColumn<BoardBean, String> colCost;

    @FXML private Button availableButton;
    @FXML private Button designButton;

    CustomOrderController customOrderController = new CustomOrderController();
    BoardBean boardBean;
    BoardBean newBoardBean;

    SceneManager sceneManager = SceneManager.getInstance();

    public void initialize() {
        customPane.setVisible(false);
        gripSlider.setMin(0.4);
        gripSlider.setMax(0.75);
        gripSlider.setValue(0.6);

        warrantySpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 12, 0, 1));
        pilesSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 2, 1, 1));
        noseSpinner.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 50, 10, 1));
        tailSpinner.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 50, 10, 1));

        gripSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            gripValueLabel.setText(String.format("Grip: %.2f", newValue.doubleValue()));
        });
        gripValueLabel.setText(String.format("Grip: %.2f", gripSlider.getValue()));

        colBoardName.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getName()));
        colDescription.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getDescription()));
        colSize.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getSize()));
        colCost.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getPrice())));
        displayAvailableBoardSamples();
    }

    public void displayAvailableBoardSamples() {
        List<BoardBean> availableBoardsList = customOrderController.getBoardSamples();
        boardTable.getItems().clear();
        boardTable.getItems().addAll(availableBoardsList);
        designButton.setStyle("-fx-background-color:  #949494;");
        availableButton.setStyle("-fx-background-color: #1ABC9C;");
    }

    public void displayBoardsCustomizedByCustomer(){
        List<BoardBean> boardBeanList = customOrderController.getCustomizedBoards();
        boardTable.getItems().clear();
        boardTable.getItems().addAll(boardBeanList);
        designButton.setStyle("-fx-background-color:  #1ABC9C;");
        availableButton.setStyle("-fx-background-color:  #949494;");
    }


    public void saveDesignBoard(){
        customOrderController.saveCreatedCustomizedBoard(newBoardBean);
    }

    public void onSampleSelected() {
        boardBean = boardTable.getSelectionModel().getSelectedItem();
        customizeLabel.setText("Customize the following sample " + boardBean.getName());
        customPane.setVisible(true);
    }

    public void logOut() {
        // implement if needed
    }

    public void generate() {
        double noseConcave = noseSpinner.getValue();
        double tailConcave = tailSpinner.getValue();
        double gripTexture = Math.floor(gripSlider.getValue() * 100) / 100.0;
        int extraPiles = pilesSpinner.getValue();
        int warrantyMonths = warrantySpinner.getValue();

        CustomBoardBean customBoardBean = new CustomBoardBean();
        customBoardBean.setName(boardBean.getName());
        customBoardBean.setConcaveNose(noseConcave);
        customBoardBean.setConcaveTail(tailConcave);
        customBoardBean.setExtraPiles(extraPiles);
        customBoardBean.setGripTexture(gripTexture);
        customBoardBean.setWarrantyMonths(warrantyMonths);

        newBoardBean = customOrderController.generateCustomBoard(customBoardBean);
        boardPriceLabel.setText("Price " + newBoardBean.getPrice());
        descriptionArea.setText(newBoardBean.getDescription());
    }

    public void goToHomePage() {
        SceneManager.getInstance().closeBro();
        try {
            SceneManager.getInstance().loadScene("viewFxml/CustomerHomePageView.fxml");
        } catch (IOException e) {
            errorLabel.setText(e.getMessage());
        }
    }

    public void goToTricksPage() {
        // implement if needed
    }
    public void orderBoard(){
        sceneManager.loadMakeOrdersPage(customOrderController, newBoardBean);
    }


    public void back() {
        customPane.setVisible(false);
        boardPriceLabel.setText("");
        descriptionArea.setText("");
    }

    public void goToCompetitionsPage() {
        // implement if needed
    }
}
