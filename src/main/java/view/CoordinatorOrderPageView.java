package view;

import beans.CustomOrderBean;
import beans.CustomOrderSummaryBean;
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
    CustomOrderBean customOrderBean;
    @FXML private TableView<CustomOrderBean> ordersTable;
    @FXML private TableColumn<CustomOrderBean, String> colDescription;
    @FXML private TableColumn<CustomOrderBean, String> colDate;
    @FXML private TableColumn<CustomOrderBean, String>  colState;
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
        List<CustomOrderBean> customOrderBeanList = customOrderController.getAllOrders();
        System.out.println("La lunghezza è"+customOrderBeanList.size());
        ordersTable.getItems().clear();
        ordersTable.getItems().addAll(customOrderBeanList);
    }

    public void loadCustomOrderSummary(CustomOrderSummaryBean customOrderSummaryBean){
        boardOrderStatusLabel.setText("Stato attuale dell'ordine "+customOrderSummaryBean.getStatus());
        boardDatesLabel.setText("Data creazione dell'ordine "+customOrderBean.getCreationDate() + "Data di conclusione"+customOrderSummaryBean.getDeliveryDate());
        boardDayEstimatedLabel.setText("Stima giorni lavorativi per la consegna "+customOrderSummaryBean.getEstimatedDays());
        boardDetailsLabel.setText("Dettagli ordine " +customOrderSummaryBean.getNameBoard()+ " "+customOrderSummaryBean.getDescriptionBoard());
        boardPriceLabel.setText("Costo totale dell'ordine " + customOrderSummaryBean.getTotalCost());
        boardDestinationLabel.setText("Destinazione dell'ordine "+customOrderSummaryBean.getRegionDestination()+ " "+customOrderSummaryBean.getProvinceDestination()+" "+customOrderSummaryBean.getCityDestination()+" "+customOrderSummaryBean.getStreetAddersDestination());
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
        customOrderController.acceptCustomOrder(customOrderBean, true);
        confStart();
    }

    public void rejectOrder(){
        customOrderController.acceptCustomOrder(customOrderBean, false);
        confStart();
    }

    @FXML
    public void onOrderSelected() {
        customOrderBean = ordersTable.getSelectionModel().getSelectedItem();
        CustomOrderSummaryBean customOrderSummaryBean = customOrderController.getMoreInfoOnOrder(customOrderBean);
        loadCustomOrderSummary(customOrderSummaryBean);
        if(customOrderSummaryBean.getStatus().equals("Requested")){
            acceptanceOrderPane.setVisible(true);
        }else if(customOrderSummaryBean.getStatus().equals("Processing")){
            postOrderPane.setVisible(true);
        }else if(customOrderSummaryBean.getStatus().equals("Completed")){

        }else{

        }
    }


    public void postNote(){
        String comment = noteArea.getText();
        try{
            if(comment == null) {
                throw new EmptyFieldException("Inserisci una nota");
            }
            ProgressNoteBean progressNoteBean = new ProgressNoteBean();
            progressNoteBean.setComment(comment);
            if(checkComplete.isSelected()){
                customOrderController.completeOrder( customOrderBean, progressNoteBean);
            }else{
                customOrderController.writeNote(customOrderBean, progressNoteBean);
            }
            confStart();
        }catch (EmptyFieldException e){
            errorLabel.setText(e.getMessage());
        }

    }




    public void newOrder(){
        loadOrderS();
    }
}
