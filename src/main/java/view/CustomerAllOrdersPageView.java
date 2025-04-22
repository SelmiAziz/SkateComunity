package view;

import beans.CustomOrderBean;
import beans.CustomOrderSummaryBean;
import beans.ProgressNoteBean;
import controls.CustomOrderController;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import utils.SceneManager;

import java.io.IOException;
import java.util.List;

public class CustomerAllOrdersPageView {

    public CustomOrderController customOrderController;

    @FXML
    private TableView<CustomOrderBean> ordersTable;
    @FXML private TableColumn<CustomOrderBean, String> colDescription;
    @FXML private TableColumn<CustomOrderBean, String> colDate;
    @FXML private TableColumn<CustomOrderBean, String>  colState;

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

    CustomOrderSummaryBean customOrderBean;


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

    public void initAfter(){
        loadCustomOrderSummary();
        loadOrdersSubmitted();
    }

    public void loadCustomOrderSummary(){
       boardOrderStatusLabel.setText("Stato attuale dell'ordine "+customOrderBean.getStatus());
       boardDatesLabel.setText("Data creazione dell'ordine "+customOrderBean.getCreationDate() + "Data di conclusione"+customOrderBean.getDeliveryDate());
       boardDayEstimatedLabel.setText("Stima giorni lavorativi per la consegna "+customOrderBean.getEstimatedDays());
       boardDetailsLabel.setText("Dettagli ordine " +customOrderBean.getNameBoard()+ " "+customOrderBean.getDescriptionBoard());
       boardPriceLabel.setText("Costo totale dell'ordine " + customOrderBean.getTotalCost());
       boardDestinationLabel.setText("Destinazione dell'ordine "+customOrderBean.getRegionDestination()+ " "+customOrderBean.getProvinceDestination()+" "+customOrderBean.getCityDestination()+" "+customOrderBean.getStreetAddersDestination());
    }


    public void displayNotes(){
        notesPane.setVisible(true);
        List<ProgressNoteBean>progressNoteBeanList = customOrderController.progressNoteBeanList(customOrderBean);
        notesList.getItems().clear();
        for(ProgressNoteBean progressNoteBean: progressNoteBeanList){
            String note = String.format(
                    "<<Comment: %s Date: %s",
                    progressNoteBean.getComment(),
                    progressNoteBean.getDate()
            );
            notesList.getItems().add(note);

        }
        backButton.setVisible(true);
        notesButton.setVisible(false);
    }

    public void setCustomOrderController(CustomOrderController customOrderController) {
        this.customOrderController = customOrderController;
    }

    public void setCustomOrderBean(CustomOrderSummaryBean customOrderBean){
        this.customOrderBean = customOrderBean;
    }


    public void loadOrdersSubmitted(){
        List<CustomOrderBean> customOrderBeanList = customOrderController.getOrdersSubmitted();
        ordersTable.getItems().clear();
        ordersTable.getItems().addAll(customOrderBeanList);
    }


    public void orderUpdate(){
        loadOrdersSubmitted();
    }

    public void back(){
        notesPane.setVisible(false);
        notesButton.setVisible(true);
    }


    public void goToHomePage(){
        SceneManager.getInstance().closeBro();
        try {
            SceneManager.getInstance().loadScene("viewFxml/CustomerHomePageView.fxml");
        } catch (IOException e) {
            errorLabel.setText(e.getMessage());
        }
    }

    public void goToTricksPage(){}
    public void goToCompetitionsPage(){}
    public void logOut(){}




}
