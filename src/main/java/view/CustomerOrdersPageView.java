package view;


import beans.CustomSkateboardBean;
import beans.SkateboardBean;
import controls.CustomOrderController;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import utils.SceneManager;

import java.io.IOException;
import java.util.List;

public class CustomerOrdersPageView {

    @FXML private Pane customPane;
    @FXML private Label skateboardPriceLabel;
    @FXML private TextArea descriptionArea;
    @FXML private Slider noiseSlider;
    @FXML private Label customizeLabel;
    @FXML private Slider gripSlider;
    @FXML private Label errorLabel;
    @FXML private Spinner<Integer> warrantySpinner;
    @FXML private Spinner<Integer> pilesSpinner;
    @FXML private Spinner<Double> noseSpinner;
    @FXML private Spinner<Double> tailSpinner;
    @FXML private Label gripValueLabel;
    @FXML private TableView<SkateboardBean> skateboardTable;
    @FXML private TableColumn<SkateboardBean, String> colSkateboardName;
    @FXML private TableColumn<SkateboardBean, String> colDescription;
    @FXML private TableColumn<SkateboardBean, String> colSize;
    @FXML private TableColumn<SkateboardBean, String> colCost;

    CustomOrderController customOrderController = new CustomOrderController();
    SkateboardBean skateboardBean ;

    public void initialize(){
        customPane.setVisible(false);
        gripSlider.setMin(0.4);
        gripSlider.setMax(0.75);
        gripSlider.setValue(0.6);

        warrantySpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 12, 0, 1));

        pilesSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1,2,1,1));

        noseSpinner.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 50, 10, 1));

        tailSpinner.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 50, 10, 1));

        gripSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            gripValueLabel.setText(String.format("Grip: %.2f", newValue.doubleValue()));
        });

        gripValueLabel.setText(String.format("Grip: %.2f", gripSlider.getValue()));




        colSkateboardName.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getName()));

        colDescription.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getDescription()));

        colSize.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getSize()));

        colCost.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getPrice())));
        loadSamples();
    }


    public void loadSamples(){
        List<SkateboardBean> availableSkateboardsList = customOrderController.getSkateboardSamples();
        skateboardTable.getItems().clear();
        skateboardTable.getItems().addAll(availableSkateboardsList);
    }


    public void onSampleSelected(){
       skateboardBean =  skateboardTable.getSelectionModel().getSelectedItem();
       customizeLabel.setText("Customize the following sample "+skateboardBean.getName());
       customPane.setVisible(true);
    }


    public void logOut(){

    }

    public void generate(){
        double noseConcave = noseSpinner.getValue();
        double tailConcave = tailSpinner.getValue();
        double gripTexture = Math.floor(gripSlider.getValue() * 100) / 100.0;
        int extraPiles = pilesSpinner.getValue();
        int warrantyMonths = warrantySpinner.getValue();
        int noiseReduction = 10;


        CustomSkateboardBean customSkateboardBean = new  CustomSkateboardBean();
        customSkateboardBean.setName(skateboardBean.getName());
        customSkateboardBean.setConcaveNose(noseConcave);
        customSkateboardBean.setConcaveTail(tailConcave);
        customSkateboardBean.setExtraPiles(extraPiles);
        customSkateboardBean.setGripTexture(gripTexture);
        customSkateboardBean.setWarrantyMonths(warrantyMonths);
        customSkateboardBean.setNoiseReduction(noiseReduction);

        SkateboardBean newSkateboardBean = customOrderController.generateModel(customSkateboardBean);
        skateboardPriceLabel.setText("Price "+newSkateboardBean.getPrice());
        descriptionArea.setText(newSkateboardBean.getDescription());


    }

    public void goToHomePage(){
        SceneManager.getInstance().closeBro();
        try {
            SceneManager.getInstance().loadScene("viewFxml/CustomerHomePageView.fxml");
        } catch (IOException e) {
             errorLabel.setText(e.getMessage());
        }
    }

    public void goToTricksPage(){

    }

    public void makeOrder(){

    }

    public void back(){
        customPane.setVisible(false);
        skateboardPriceLabel.setText("");
        descriptionArea.setText("");
    }


    public void goToCompetitionsPage(){

    }
}
