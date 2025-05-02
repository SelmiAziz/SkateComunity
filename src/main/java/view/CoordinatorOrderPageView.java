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

import java.util.List;

public class CoordinatorOrderPageView {
    CustomOrderController customOrderController ;

    public void setCustomOrderController(CustomOrderController customOrderController){
        this.customOrderController = customOrderController;
    }
    OrderSummaryBean orderSummaryBean;
    @FXML private TableView<OrderSummaryBean> ordersTable;
    @FXML private TableColumn<OrderSummaryBean, String> colDescription;
    @FXML private TableColumn<OrderSummaryBean, String> colDate;
    @FXML private TableColumn<OrderSummaryBean, String>  colState;
    @FXML private CheckBox checkComplete;

    @FXML private Label boardDetailsLabel;
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

    }

    public void loadOrderS(){
        List<OrderSummaryBean> customOrderBeanList = customOrderController.getAllOrders();
        ordersTable.getItems().clear();
        ordersTable.getItems().addAll(customOrderBeanList);
    }

    public void loadCustomOrderSummary(OrderSummaryBean orderSummaryBean){
        boardOrderStatusLabel.setText("Stato attuale dell'ordine "+orderSummaryBean.getStatus());
        boardDatesLabel.setText("Data creazione dell'ordine "+orderSummaryBean.getCreationDate() + "Data di conclusione"+orderSummaryBean.getDeliveryDate());
        boardDayEstimatedLabel.setText("Stima giorni lavorativi per la consegna "+orderSummaryBean.getEstimatedDays());
        boardDetailsLabel.setText("Dettagli ordine " +orderSummaryBean.getNameBoard()+ " "+orderSummaryBean.getDescriptionBoard());
        boardPriceLabel.setText("Costo totale dell'ordine " + orderSummaryBean.getTotalCost());
        boardDestinationLabel.setText("Destinazione dell'ordine "+orderSummaryBean.getRegionDestination()+ " "+orderSummaryBean.getProvinceDestination()+" "+orderSummaryBean.getCityDestination()+" "+orderSummaryBean.getStreetAddersDestination());
    }

    public void confStart(){
        acceptanceOrderPane.setVisible(false);
        postOrderPane.setVisible(false);
        boardDatesLabel.setText("");
        boardDayEstimatedLabel.setText("");
        boardDestinationLabel.setText("");
        boardPriceLabel.setText("");
        boardOrderStatusLabel.setText("");
        boardDetailsLabel.setText("");
    }

    public void acceptOrder(){
        OrderBean orderBean = new OrderBean();
        orderBean.setId(orderSummaryBean.getId());
        customOrderController.acceptCustomOrder(orderBean, true);
        confStart();
        loadOrderS();
    }

    public void rejectOrder(){
        OrderBean orderBean = new OrderBean();
        orderBean.setId(orderSummaryBean.getId());
        customOrderController.acceptCustomOrder(orderBean, false);
        confStart();
        loadOrderS();
    }

    @FXML
    public void onOrderSelected() {
        confStart();
        orderSummaryBean = ordersTable.getSelectionModel().getSelectedItem();
        loadCustomOrderSummary(orderSummaryBean);
        if(orderSummaryBean.getStatus().equals("Requested")){
            acceptanceOrderPane.setVisible(true);
        }else if(orderSummaryBean.getStatus().equals("Processing")){
            postOrderPane.setVisible(true);
        }else if(orderSummaryBean.getStatus().equals("Completed")){

        }else{

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
            loadOrderS();
        }catch (EmptyFieldException e){
            errorLabel.setText(e.getMessage());
        }

    }




    public void newOrder(){
        loadOrderS();
    }
}
