package view;

import beans.BoardBean;
import beans.CustomBoardBean;
import beans.BoardProfileBean;
import beans.WalletBean;
import controls.CustomOrderController;
import exceptions.SessionExpiredException;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import utils.CustomerOrderView;
import utils.WindowManager;

import java.io.IOException;
import java.util.List;

public class CustomerBoardPageView implements CustomerOrderView {

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
    @FXML private TableView<BoardProfileBean> boardTable;
    @FXML private TableColumn<BoardProfileBean, String> colBoardName;
    @FXML private TableColumn<BoardProfileBean, String> colDescription;
    @FXML private TableColumn<BoardProfileBean, String> colSize;
    @FXML private TableColumn<BoardProfileBean, String> colCost;
    @FXML private Pane pannelPane;
    @FXML private Label priceBoardLabel;
    @FXML private Label sizeBoardLabel;
    @FXML private Label descriptionBoardLabel;
    @FXML private Label nameBoardLabel;
    @FXML private Pane orderPane;

    @FXML private CheckBox noseCheck;
    @FXML private CheckBox tailCheck;
    @FXML private CheckBox pilesCheck;
    @FXML private CheckBox warrantyCheck;
    @FXML private CheckBox gripCheck;

    @FXML private Button availableButton;
    @FXML private Button designButton;
    @FXML private Button saveBoardButton;
    @FXML private Label statusLabel;
    @FXML private Label coinsLabel;


    WindowManager windowManager = WindowManager.getInstance();
    CustomOrderController customOrderController = new CustomOrderController();
    BoardProfileBean customizedBoardBean;
    BoardBean boardBean;


    public void initialize()  {
        customPane.setVisible(false);
        gripSlider.setMin(0.4);
        gripSlider.setMax(0.75);
        gripSlider.setValue(0.6);

        warrantySpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 12, 0, 1));
        pilesSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 2, 1, 1));
        noseSpinner.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 50, 10, 1));
        tailSpinner.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 50, 10, 1));


        gripSlider.valueProperty().addListener((observable, oldValue, newValue) ->
                gripValueLabel.setText(String.format("Grip: %.2f", newValue.doubleValue()))
        );

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
        displayWallet();
    }

    public CustomOrderController getController(){
        return this.customOrderController;
    }

    public void initConf(){
        pannelPane.setVisible(false);
        saveBoardButton.setVisible(true);
        customPane.setVisible(false);
        orderPane.setVisible(false);
        boardPriceLabel.setText("");
        descriptionArea.setText("");
    }

    public void orderUpdate(){
        windowManager.loadPreviousOrdersPage();
    }

    public void displayAvailableBoardSamples()  {
        try {
            List<BoardProfileBean> availableBoardsList = customOrderController.getBoardSamples(windowManager.getAuthBean().getToken());
            statusLabel.setText("available");
            boardTable.getItems().clear();
            boardTable.getItems().addAll(availableBoardsList);
            designButton.setStyle("-fx-background-color:  #949494;");
            availableButton.setStyle("-fx-background-color: #1ABC9C;");
        }catch(SessionExpiredException _ ){
            windowManager.logOut();
        }
    }

    public void displayBoardsCustomizedByCustomer()  {
        try{
            List<BoardProfileBean> boardBeanList = customOrderController.getCustomizedBoards(windowManager.getAuthBean().getToken());
            statusLabel.setText("designed");
            boardTable.getItems().clear();
            boardTable.getItems().addAll(boardBeanList);
            designButton.setStyle("-fx-background-color:  #1ABC9C;");
            availableButton.setStyle("-fx-background-color:  #949494;");
        }catch(SessionExpiredException _){
            windowManager.logOut();
        }
    }


    public void loadBoardForOrder(BoardProfileBean customizedBoardBean){
        nameBoardLabel.setText("Board sample name: "+customizedBoardBean.getName());
        priceBoardLabel.setText("Price:  "+ customizedBoardBean.getPrice());
        sizeBoardLabel.setText("Size of the board: "+customizedBoardBean.getSize());
        descriptionBoardLabel.setText("Description" + customizedBoardBean.getDescription());
    }

    public void goPreviousOrders(){
        WindowManager.getInstance().loadPreviousOrdersPage();
    }


    public void saveDesignBoard() {
        try {
            loadBoardForOrder(customizedBoardBean);
            boardBean = customOrderController.saveCreatedCustomizedBoard(windowManager.getAuthBean().getToken(), customizedBoardBean);
            orderPane.setVisible(true);
        }catch(SessionExpiredException _ ){
            windowManager.logOut();
        }
    }

    public void onSampleSelected() {
        customizedBoardBean = boardTable.getSelectionModel().getSelectedItem();
        if(statusLabel.getText().equals("available")){
            customizeLabel.setText("Customize the following sample " + customizedBoardBean.getName());
            descriptionArea.setText("");
            orderPane.setVisible(false);
            customPane.setVisible(true);
        }else{
            boardBean = new BoardBean();
            boardBean.setId(customizedBoardBean.getId());
            loadBoardForOrder(customizedBoardBean);
            orderPane.setVisible(true);
            customPane.setVisible(true);
        }
    }

    public void logOut() {
        windowManager.closeCoordinator();
        windowManager.logOut();

    }

    public void generate()  {
        double noseConcave = noseSpinner.getValue();
        double tailConcave = tailSpinner.getValue();
        double gripTexture = Math.floor(gripSlider.getValue() * 100) / 100.0;
        int extraPiles = pilesSpinner.getValue();
        int warrantyMonths = warrantySpinner.getValue();


        CustomBoardBean customBoardBean = new CustomBoardBean();
        customBoardBean.setName(customizedBoardBean.getName());
        if(noseCheck.isSelected()){
            customBoardBean.setUseConcaveNose(true);
            customBoardBean.setConcaveNose(noseConcave);
        }else{
            customBoardBean.setUseConcaveNose(false);
        }
        if(tailCheck.isSelected()){
            customBoardBean.setConcaveTail(tailConcave);
            customBoardBean.setUseConcaveTail(true);
        }else{
            customBoardBean.setUseConcaveTail(false);
        }
        if(pilesCheck.isSelected()){
            customBoardBean.setExtraPiles(extraPiles);
            customBoardBean.setUseExtraPiles(true);
        }else{
            customBoardBean.setUseExtraPiles(false);
        }
        if(gripCheck.isSelected()){
            customBoardBean.setGripTexture(gripTexture);
            customBoardBean.setUseGripTexture(true);
        }else{
            customBoardBean.setUseGripTexture(false);
        }
        if(warrantyCheck.isSelected()){
            customBoardBean.setWarrantyMonths(warrantyMonths);
            customBoardBean.setUseWarrantyMonths(true);
        }else{
            customBoardBean.setUseWarrantyMonths(false);
        }

        try {
            customizedBoardBean = customOrderController.generateCustomBoard(windowManager.getAuthBean().getToken(), customBoardBean);
            boardPriceLabel.setText("Price " + customizedBoardBean.getPrice());
            descriptionArea.setText(customizedBoardBean.getDescription());
            pannelPane.setVisible(true);
        }catch(SessionExpiredException _){
            windowManager.logOut();
        }
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
        windowManager.loadMakeOrdersPage(boardBean);
    }


    public void displayWallet(){
        try {
            WalletBean walletBean = customOrderController.walletDetails(windowManager.getAuthBean().getToken());
            coinsLabel.setText("" + walletBean.getBalance());
        }catch(SessionExpiredException _){
            windowManager.logOut();
        }
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
