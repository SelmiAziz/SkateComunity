package view;

import beans.OrderBean;
import beans.OrderSummaryBean;
import beans.ProgressNoteBean;
import controls.CustomOrderController;
import exceptions.EmptyFieldException;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import utils.CoordinatorOrderView;

import java.util.List;

public class CoordinatorOrderPageView implements CoordinatorOrderView {
    CustomOrderController customOrderController ;

    public void setController(CustomOrderController customOrderController){
        this.customOrderController = customOrderController;
    }
    OrderSummaryBean orderSummaryBean;
    @FXML private TableView<OrderSummaryBean> ordersTable;
    @FXML private TableColumn<OrderSummaryBean, String> colDescription;
    @FXML private TableColumn<OrderSummaryBean, String> colDate;
    @FXML private TableColumn<OrderSummaryBean, String>  colState;
    @FXML private CheckBox checkComplete;

    @FXML private TextArea detailsArea;
    @FXML private Label boardDatesLabel;
    @FXML private Label boardDayEstimatedLabel;
    @FXML private Label boardPriceLabel;
    @FXML private Label boardDestinationLabel;
    @FXML private Label boardOrderStatusLabel;
    @FXML private Label errorLabel;
    @FXML private Pane acceptanceOrderPane;
    @FXML private Pane postOrderPane;
    @FXML private TextArea noteArea;
    @FXML private TextField dateField;

    public void initialize(){
        colDescription.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getDescriptionBoard()));

        colDate.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getCreationDate()));

        colState.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getStatus()));

        confStart();
        detailsArea.setEditable(false);

    }

    public void loadOrders(){
        List<OrderSummaryBean> customOrderBeanList = customOrderController.getAllOrders();
        ordersTable.getItems().clear();
        ordersTable.getItems().addAll(customOrderBeanList);
    }

    public void loadCustomOrderSummary(OrderSummaryBean orderSummaryBean){
        boardOrderStatusLabel.setText("Current order status: " + orderSummaryBean.getStatus());
        boardDatesLabel.setText("Order creation date: " + orderSummaryBean.getCreationDate());
        boardDayEstimatedLabel.setText("Estimated business days for delivery: " + orderSummaryBean.getEstimatedDays());
        detailsArea.setText("Order details: " + orderSummaryBean.getNameBoard() + " " + orderSummaryBean.getDescriptionBoard());
        boardPriceLabel.setText("Total order cost: " + orderSummaryBean.getTotalCost());
        boardDestinationLabel.setText("Order destination: " + orderSummaryBean.getRegionDestination() + " " + orderSummaryBean.getProvinceDestination() + " " + orderSummaryBean.getCityDestination() + " " + orderSummaryBean.getStreetAddersDestination());

    }

    public void confStart(){
        acceptanceOrderPane.setVisible(false);
        postOrderPane.setVisible(false);
        boardDatesLabel.setText("");
        boardDayEstimatedLabel.setText("");
        boardDestinationLabel.setText("");
        boardPriceLabel.setText("");
        boardOrderStatusLabel.setText("");
        detailsArea.setText("");
        detailsArea.setVisible(false);
    }

    public void acceptOrder(){
        OrderBean orderBean = new OrderBean();
        orderBean.setId(orderSummaryBean.getId());
        customOrderController.acceptCustomOrder(orderBean, true);
        confStart();
        loadOrders();
    }

    public void rejectOrder(){
        OrderBean orderBean = new OrderBean();
        orderBean.setId(orderSummaryBean.getId());
        customOrderController.acceptCustomOrder(orderBean, false);
        confStart();
        loadOrders();
    }

    @FXML
    public void onOrderSelected() {
        confStart();
        orderSummaryBean = ordersTable.getSelectionModel().getSelectedItem();
        loadCustomOrderSummary(orderSummaryBean);
        detailsArea.setVisible(true);
        if(orderSummaryBean.getStatus().equals("Requested")){
            acceptanceOrderPane.setVisible(true);
        }else if(orderSummaryBean.getStatus().equals("Processing")) {
            postOrderPane.setVisible(true);
        }
    }


    public void postNote(){
        String comment = noteArea.getText();
        String date = dateField.getText();
        try{
            if(comment == null || date == null) {
                throw new EmptyFieldException("Inserisci una nota");
            }
            ProgressNoteBean progressNoteBean = new ProgressNoteBean();
            progressNoteBean.setComment(comment);
            progressNoteBean.setDate(date);
            OrderBean orderBean = new OrderBean();
            orderBean.setId(orderSummaryBean.getId());
            if(checkComplete.isSelected()){
                customOrderController.completeOrder( orderBean, progressNoteBean);
            }else{
                customOrderController.writeNote(orderBean, progressNoteBean);
            }
            confStart();
            dateField.setText("");
            noteArea.setText("");
            loadOrders();
        }catch (EmptyFieldException e){
            errorLabel.setText(e.getMessage());
        }

    }


    @Override
    public void newOrder(){
        loadOrders();
    }
}
