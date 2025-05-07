package viewBasic;

import beans.BoardProfileBean;
import beans.OrderBean;
import beans.OrderSummaryBean;
import beans.ProgressNoteBean;
import controls.CustomOrderController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import utils.CustomerOrderView;
import utils.WindowManagerBasic;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomerOrdersPageViewBasic implements CustomerOrderView {
    private CustomOrderController customOrderController;
    private WindowManagerBasic windowManagerBasic = WindowManagerBasic.getInstance();

    private final Map<Integer, String> boardIdMap = new HashMap<>();
    private final Map<Integer, String> orderIdMap = new HashMap<>();


    @FXML private ChoiceBox<String> choicePage;
    @FXML private Pane designPane;
    @FXML private Pane notesPane;
    @FXML private Pane boardPane;
    @FXML private Pane orderSubmitPane;
    @FXML private Pane orderMonitorPane;
    @FXML private ListView<String> allListView;
    @FXML private ListView<String> notesListView;
    @FXML private TextField numberBoardField;
    @FXML private TextField numberOrderField;

    public void initialize(){
        populatePageChoice();
        confStart();
    }

    public void setController(CustomOrderController customOrderController){
        this.customOrderController = customOrderController;
    }


    public void initializeAfter(){
        //displayDesignedBoards();
        displayOrders();
    }

    public void confStart(){
        //orderSubmitPane.setVisible(false);
        //orderMonitorPane.setVisible(false);
        //boardPane.setVisible(false);
    }

    private void populatePageChoice() {
        List<String> list = Arrays.asList( "Learn", "Competitions", "Log Out");
        ObservableList<String> pages = FXCollections.observableArrayList(list);
        choicePage.setItems(pages);
        choicePage.setValue("Board");
    }

    @Override
    public void orderUpdate() {

    }

    public void changePage(){

    }

    public void designConf(){

    }

    public void ordersConf(){

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


    public void displayProgressNotes() {
        System.out.println("Andando");
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


    public void displayDesignedBoards(){
        List<BoardProfileBean> boards = customOrderController.getCustomizedBoards(windowManagerBasic.getAuthBean().getToken());
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



    public void displayAvailableBoards(){

    }

    public void displayBoard(){
        int n = Integer.parseInt(numberBoardField.getText());
        System.out.println(boardIdMap.get(n));
    }


}
