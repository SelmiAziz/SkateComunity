package view;

import beans.CompetitionBean;
import beans.CustomOrderBean;
import controls.CustomOrderController;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import utils.SceneManager;

import java.io.IOException;

public class CustomerAllOrdersPageView {

    public CustomOrderController customOrderController;

    @FXML
    private TableView<CustomOrderBean> ordersTable;
    @FXML private TableColumn<CustomOrderBean, String> colName;
    @FXML private TableColumn<CustomOrderBean, String> colDescription;
    @FXML private TableColumn<CustomOrderBean, String> colDate;
    @FXML private TableColumn<CustomOrderBean, String>  colState;
    @FXML private TableColumn<CompetitionBean, String> colCoins;

    @FXML private Label boardDetailsLabel;
    @FXML private Label boardDatesLabel;
    @FXML private Label boardDayEstimatedLabel;
    @FXML private Label boardDestinationLabel;
    @FXML private Label boardOrderStatusLabel;
    @FXML private Label errorLabel;


    public void initialize(){
        colName.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getNameBoard()));

        colDescription.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getDescriptionBoard()));

        colDate.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getCreationDate()));

        colState.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getStatus()));

        colCoins.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getCoins())));
    }

    public void setCustomOrderController(CustomOrderController customOrderController) {
        this.customOrderController = customOrderController;
    }

    public void back(){

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
