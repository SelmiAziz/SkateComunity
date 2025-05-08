package viewBasic;

import beans.BoardProfileBean;
import beans.OrderBean;
import beans.OrderSummaryBean;
import beans.ProgressNoteBean;
import controls.CustomOrderController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import utils.CustomerOrderView;
import utils.WindowManagerBasic;

import java.io.IOException;
import java.util.*;

public class CustomerOrdersPageViewBasic implements CustomerOrderView {
    private CustomOrderController customOrderController;
    private WindowManagerBasic windowManagerBasic = WindowManagerBasic.getInstance();

    private final Map<Integer, String> boardIdMap = new HashMap<>();
    private final Map<Integer, String> orderIdMap = new HashMap<>();


    @FXML private ChoiceBox<String> choicePage;
    @FXML private Pane designControlPane;
    @FXML private Pane orderControlPane;
    @FXML private Pane notesPane;
    @FXML private Pane boardPane;
    //@FXML private Pane orderSubmitPane;
    @FXML private ListView<String> allListView;
    @FXML private ListView<String> notesListView;
    @FXML private TextField numberBoardField;
    @FXML private TextField numberOrderField;


    @FXML private Button orderButton;
    @FXML private Button boardButton;

    @FXML private Button availableButton;
    @FXML private Button designButton;

    @FXML private ComboBox<String> noseCombo;
    @FXML private ComboBox<String> tailCombo;
    @FXML private ComboBox<String> warrantyCombo;
    @FXML private ComboBox<String> pilesCombo;
    @FXML private ComboBox<String> gripCombo;

    @FXML private TextArea resultArea;



    @FXML private Label errorLabel;

    public void initialize(){
        initGripCombo();
        initWarrantyCombo();
        initPilesCombo();
        initNoseCombo();
        initTailCombo();
        populatePageChoice();
        resultArea.setEditable(false);
        confStart();
        setButtonStyle(availableButton, "#1ABC9C");
        setButtonStyle(designButton, "B0B0B0");
    }


    private void initGripCombo() {
        List<String> gripValues = new ArrayList<>();
        for (double val = 0.40; val <= 0.75; val += 0.01) {
            gripValues.add(String.format(Locale.US, "%.2f", val));
        }
        gripCombo.setItems(FXCollections.observableArrayList(gripValues));
        gripCombo.setValue("0.60");
    }

    private void initWarrantyCombo() {
        warrantyCombo.getItems().clear();
        for (int i = 0; i <= 12; i++) {
            warrantyCombo.getItems().add(String.valueOf(i));
        }
        warrantyCombo.setValue("0");
    }

    private void initPilesCombo() {
        pilesCombo.getItems().clear();
        for (int i = 0; i <= 2; i++) {
            pilesCombo.getItems().add(String.valueOf(i));
        }
        pilesCombo.setValue("1");
    }

    private void initNoseCombo() {
        noseCombo.getItems().clear();
        for (int i = 0; i <= 50; i++) {
            noseCombo.getItems().add(String.valueOf(i));
        }
        noseCombo.setValue("10");
    }

    private void initTailCombo() {
        tailCombo.getItems().clear();
        for (int i = 0; i <= 50; i++) {
            tailCombo.getItems().add(String.valueOf(i));
        }
        tailCombo.setValue("10");
    }

    public void setController(CustomOrderController customOrderController){
        this.customOrderController = customOrderController;
    }


    public void initializeAfter(){
        displayAvailableBoards();
        confStart();
    }

    public void confStart(){
        //orderSubmitPane.setVisible(false);
        boardPane.setVisible(false);
        notesPane.setVisible(false);
        orderControlPane.setVisible(false);
        designControlPane.setVisible(true);
    }

    private void setButtonStyle(Button button, String colorHex) {
        button.setStyle("-fx-background-color: " + colorHex + "; -fx-text-fill: white;");
    }


    private void populatePageChoice() {
        List<String> list = Arrays.asList( "Learn", "Competitions", "Log Out");
        ObservableList<String> pages = FXCollections.observableArrayList(list);
        choicePage.setItems(pages);
        choicePage.setValue("Board");
    }

    @Override
    public void orderUpdate() {
        displayOrders();
    }

    public void changePage(){
        String page = choicePage.getValue();
        if(page.equals("Competitions")){
            try {
                windowManagerBasic.goToCustomerCompetitions();
            } catch(IOException e){
                errorLabel.setText(e.getMessage());
            }
        }else if(page.equals("Learn")){
            try{
                windowManagerBasic.goToLearn();
            }catch(IOException e){
                errorLabel.setText(e.getMessage());
            }
        }else if(page.equals("Log Out")){
            try {
                windowManagerBasic.logOut();
            } catch(IOException e){
                errorLabel.setText(e.getMessage());
            }
        }
    }

    public void boardConf(){
        setButtonStyle(boardButton,"#1ABC9C" );
        setButtonStyle(orderButton, "B0B0B0");
        confStart();
        displayAvailableBoards();
    }

    public void process(){

    }


    public void orderConf(){
        setButtonStyle(orderButton,"#1ABC9C" );
        setButtonStyle(boardButton, "B0B0B0");
        notesPane.setVisible(true);
        designControlPane.setVisible(false);
        orderControlPane.setVisible(true);
        displayOrders();
    }

    public void displayOrders() {
        List<OrderSummaryBean> orders = customOrderController.getSubmittedOrders(windowManagerBasic.getAuthBean().getToken());
        ObservableList<String> items = FXCollections.observableArrayList();
        orderIdMap.clear();
        int index = 1;

        for (OrderSummaryBean order : orders) {
            String displayText = String.format(
                    "[%d] %s - %s | €%d | Status: %s | Delivery: %s | Destination: %s, %s, %s, %s",
                    index,
                    order.getNameBoard(),
                    order.getDescriptionBoard(),
                    order.getTotalCost(),
                    order.getStatus(),
                    order.getDeliveryDate(),
                    order.getStreetAddersDestination(),
                    order.getCityDestination(),
                    order.getProvinceDestination(),
                    order.getRegionDestination()
            );

            items.add(displayText);
            orderIdMap.put(index, order.getId());
            index++;
        }

        allListView.setItems(items);
    }

    public void displayDesignedBoards() {
        List<BoardProfileBean> boards = customOrderController.getCustomizedBoards(windowManagerBasic.getAuthBean().getToken());
        updateBoardList(boards, designButton, availableButton);
    }

    public void displayAvailableBoards() {
        List<BoardProfileBean> boards = customOrderController.getBoardSamples(windowManagerBasic.getAuthBean().getToken());
        updateBoardList(boards, availableButton, designButton);
    }

    private void updateBoardList(List<BoardProfileBean> boards, Button active, Button inactive) {
        setButtonStyle(active, "#1ABC9C");
        setButtonStyle(inactive, "#B0B0B0");

        ObservableList<String> items = FXCollections.observableArrayList();
        boardIdMap.clear();

        int index = 1;
        for (BoardProfileBean board : boards) {
            String displayText = String.format("[%d] %s - %s | €%d | Size: %s",
                    index,
                    board.getName(),
                    board.getDescription(),
                    board.getPrice(),
                    board.getSize()
            );
            items.add(displayText);
            boardIdMap.put(index, board.getId());
            index++;
        }

        allListView.setItems(items);
    }


    public void displayProgressNotes() {
        ObservableList<String> items = FXCollections.observableArrayList();
        int n = Integer.parseInt(numberOrderField.getText());
        String orderId = orderIdMap.get(n);
        OrderBean orderBean = new  OrderBean();
        orderBean.setId(orderId);
        List<ProgressNoteBean> progressNoteBeanList = customOrderController.getProgressNotesOrder(windowManagerBasic.getAuthBean().getToken(), orderBean);

        for (ProgressNoteBean progressNoteBean : progressNoteBeanList) {
            String displayText = String.format("%s | %s",
                    progressNoteBean.getDate(),
                    progressNoteBean.getComment()
            );
            items.add(displayText);
        }

        notesListView.setItems(items);
    }



    public void displayBoard(){
        int n = Integer.parseInt(numberBoardField.getText());
        boardPane.setVisible(true);
        //resultArea.setText(
               // "Price: " + customizedBoardBean.getPrice() + "\n\n" +
                   //     customizedBoardBean.getDescription()
        //);

    }


}
