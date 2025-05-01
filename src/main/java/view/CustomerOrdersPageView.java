package view;

import beans.AuthTokenBean;
import beans.BoardBean;
import beans.CustomBoardBean;
import beans.CustomizedBoardBean;
import controls.CustomOrderController;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import utils.WindowManager;

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
    @FXML private TableView<CustomizedBoardBean> boardTable;
    @FXML private TableColumn<CustomizedBoardBean, String> colBoardName;
    @FXML private TableColumn<CustomizedBoardBean, String> colDescription;
    @FXML private TableColumn<CustomizedBoardBean, String> colSize;
    @FXML private TableColumn<CustomizedBoardBean, String> colCost;
    @FXML private Pane pannelPane;
    @FXML private Label priceBoardLabel;
    @FXML private Label sizeBoardLabel;
    @FXML private Label descriptionBoardLabel;
    @FXML private Label nameBoardLabel;
    @FXML private Pane orderPane;

    @FXML private Button availableButton;
    @FXML private Button designButton;
    @FXML private Button saveBoardButton;
    @FXML private Label statusLabel;


    WindowManager windowManager = WindowManager.getInstance();
    CustomOrderController customOrderController = new CustomOrderController();
    CustomizedBoardBean customizedBoardBean;
    BoardBean boardBean;


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
        initConf();
    }

    public void initConf(){
        pannelPane.setVisible(false);
        saveBoardButton.setVisible(true);
        customPane.setVisible(false);
        orderPane.setVisible(false);
        boardPriceLabel.setText("");
        descriptionArea.setText("");
    }

    public void displayAvailableBoardSamples() {
        AuthTokenBean authTokenBean = new AuthTokenBean();
        authTokenBean.setToken(windowManager.getAuthBean().getToken());
        List<CustomizedBoardBean> availableBoardsList = customOrderController.getBoardSamples(authTokenBean);
        statusLabel.setText("available");
        boardTable.getItems().clear();
        boardTable.getItems().addAll(availableBoardsList);
        designButton.setStyle("-fx-background-color:  #949494;");
        availableButton.setStyle("-fx-background-color: #1ABC9C;");
    }

    public void displayBoardsCustomizedByCustomer(){
        AuthTokenBean authTokenBean = new AuthTokenBean();
        authTokenBean.setToken(windowManager.getAuthBean().getToken());
        List<CustomizedBoardBean> boardBeanList = customOrderController.getCustomizedBoards(authTokenBean);
        statusLabel.setText("designed");
        boardTable.getItems().clear();
        boardTable.getItems().addAll(boardBeanList);
        designButton.setStyle("-fx-background-color:  #1ABC9C;");
        availableButton.setStyle("-fx-background-color:  #949494;");
    }


    public void loadBoardForOrder(CustomizedBoardBean customizedBoardBean){
        nameBoardLabel.setText("Board sample name: "+customizedBoardBean.getName());
        priceBoardLabel.setText("Price:  "+ customizedBoardBean.getPrice());
        sizeBoardLabel.setText("Size of the board: "+customizedBoardBean.getSize());
        descriptionBoardLabel.setText("Description" + customizedBoardBean.getDescription());
    }

    public void goPreviousOrders(){
        WindowManager.getInstance().loadPreviousOrdersPage(customOrderController);
    }


    public void saveDesignBoard(){
        loadBoardForOrder(customizedBoardBean);
        AuthTokenBean authTokenBean = new AuthTokenBean();
        authTokenBean.setToken(windowManager.getAuthBean().getToken());
        boardBean = customOrderController.saveCreatedCustomizedBoard(customizedBoardBean,authTokenBean);
        orderPane.setVisible(true);
    }

    public void onSampleSelected() {
        customizedBoardBean = boardTable.getSelectionModel().getSelectedItem();
        if(statusLabel.getText().equals("available")){
            customizeLabel.setText("Customize the following sample " + customizedBoardBean.getName());
            customPane.setVisible(true);
        }else{
            boardBean = new BoardBean();
            boardBean.setId(customizedBoardBean.getId());
            loadBoardForOrder(customizedBoardBean);
            orderPane.setVisible(true);
        }
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
        customBoardBean.setName(customizedBoardBean.getName());
        customBoardBean.setConcaveNose(noseConcave);
        customBoardBean.setConcaveTail(tailConcave);
        customBoardBean.setExtraPiles(extraPiles);
        customBoardBean.setGripTexture(gripTexture);
        customBoardBean.setWarrantyMonths(warrantyMonths);

        AuthTokenBean authTokenBean = new AuthTokenBean();
        authTokenBean.setToken(windowManager.getAuthBean().getToken());
        customizedBoardBean = customOrderController.generateCustomBoard(customBoardBean,authTokenBean);
        boardPriceLabel.setText("Price " +customizedBoardBean.getPrice());
        descriptionArea.setText(customizedBoardBean.getDescription());
        pannelPane.setVisible(true);
    }



    public void goToHomePage() {
       windowManager.closeCoordinator();
        try {
            windowManager.goToHomePage();
        } catch (IOException e) {
            errorLabel.setText(e.getMessage());
        }
    }

    public void goToTricksPage() {
        try {
            windowManager.goToLearn();
        } catch (IOException e) {
            errorLabel.setText(e.getMessage());
        }
    }
    public void orderBoard(){
        windowManager.loadMakeOrdersPage(customOrderController, boardBean);
    }


    public void back() {
        initConf();
    }

    public void goToCompetitionsPage() {
        try {
            windowManager.goToCustomerCompetitions();
        } catch (IOException e) {
            errorLabel.setText(e.getMessage());
        }
    }
}
