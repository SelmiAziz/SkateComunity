package view;

import beans.OrderBean;
import beans.OrderSummaryBean;
import beans.ProgressNoteBean;
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

public class CustomerAllOrdersPageView implements CustomerOrderView {

    public CustomOrderController customOrderController;
    public WindowManager windowManager = WindowManager.getInstance();

    @FXML
    private TableView<OrderSummaryBean> ordersTable;
    @FXML private TableColumn<OrderSummaryBean, String> colDescription;
    @FXML private TableColumn<OrderSummaryBean, String> colDate;
    @FXML private TableColumn<OrderSummaryBean, String>  colState;

    @FXML private Label boardDetailsLabel;
    @FXML private Label boardDatesLabel;
    @FXML private Label boardDayEstimatedLabel;
    @FXML private Label boardPriceLabel;
    @FXML private Label boardDestinationLabel;
    @FXML private Label boardOrderStatusLabel;
    @FXML private Label errorLabel;
    @FXML private ListView<String> notesList;
    @FXML private Pane notesPane;
    @FXML private Button backButton;
    @FXML private Button notesButton;

    OrderSummaryBean customOrderBean;


    public void initialize(){
        colDescription.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getDescriptionBoard()));

        colDate.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getCreationDate()));

        colState.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getStatus()));


        backButton.setVisible(false);
        notesPane.setVisible(false);

    }

    public void initAfter() {
        loadCustomOrderSummary();
        loadOrdersSubmitted();
    }

    public void initAfter2() {
        loadOrdersSubmitted();
    }

    public void loadCustomOrderSummary(){
       boardOrderStatusLabel.setText("Stato attuale dell'ordine "+customOrderBean.getStatus());
       boardDatesLabel.setText("Data creazione dell'ordine "+customOrderBean.getCreationDate() + " Data di conclusione"+customOrderBean.getDeliveryDate());
       boardDayEstimatedLabel.setText("Stima giorni lavorativi per la consegna "+customOrderBean.getEstimatedDays());
       boardDetailsLabel.setText("Dettagli ordine " +customOrderBean.getNameBoard()+ " "+customOrderBean.getDescriptionBoard());
       boardPriceLabel.setText("Costo totale dell'ordine " + customOrderBean.getTotalCost());
       boardDestinationLabel.setText("Destinazione dell'ordine "+customOrderBean.getRegionDestination()+ " "+customOrderBean.getProvinceDestination()+" "+customOrderBean.getCityDestination()+" "+customOrderBean.getStreetAddersDestination());
    }


    public void displayNotes()  {
        notesPane.setVisible(true);
        OrderBean orderBean = new OrderBean();
        orderBean.setId(customOrderBean.getId());
        try {
            List<ProgressNoteBean> progressNoteBeanList = customOrderController.getProgressNotesOrder(windowManager.getAuthBean().getToken(), orderBean);
            notesList.getItems().clear();
            for (ProgressNoteBean progressNoteBean : progressNoteBeanList) {
                String note = String.format(
                        "<<Comment: %s Date: %s",
                        progressNoteBean.getComment(),
                        progressNoteBean.getDate()
                );
                notesList.getItems().add(note);

            }
            backButton.setVisible(true);
            notesButton.setVisible(false);
        }catch(SessionExpiredException e){
            windowManager.logOut();
        }
    }

    public void setCustomOrderController(CustomOrderController customOrderController) {
        this.customOrderController = customOrderController;
    }

    public void setCustomOrderBean(OrderSummaryBean customOrderBean){
        this.customOrderBean = customOrderBean;
    }


    public void loadOrdersSubmitted()  {
        try {
            List<OrderSummaryBean> customOrderBeanList = customOrderController.getSubmittedOrders(windowManager.getAuthBean().getToken());
            ordersTable.getItems().clear();
            ordersTable.getItems().addAll(customOrderBeanList);
        }catch(SessionExpiredException e){
            windowManager.logOut();
        }
    }

    public void selectOrder(){
        customOrderBean = ordersTable.getSelectionModel().getSelectedItem();
        loadCustomOrderSummary();
    }


    @Override
    public void orderUpdate() {
        loadOrdersSubmitted();
    }

    public void back(){
        notesPane.setVisible(false);
        backButton.setVisible(false);
        notesButton.setVisible(true);
    }


    public void goToHomePage(){
        windowManager.closeCoordinator();
        try {
            windowManager.goToHomePage();
        }catch(IOException e){
            errorLabel.setText(e.getMessage());
        }
    }

    public void goToTricksPage(){
        windowManager.closeCoordinator();
        try {
            windowManager.goToLearn();
        }catch(IOException e){
            errorLabel.setText(e.getMessage());
        }
    }
    public void goToCompetitionsPage(){
        windowManager.closeCoordinator();
        try {
            windowManager.goToCustomerCompetitions();
        }catch(IOException e){
            errorLabel.setText(e.getMessage());
        }
    }
    public void logOut(){
        windowManager.closeCoordinator();
        try {
            windowManager.goToHomePage();
        }catch(IOException e){
            errorLabel.setText(e.getMessage());
        }

    }




}
