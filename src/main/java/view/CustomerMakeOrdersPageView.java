package view;

import beans.DeliveryDestinationBean;
import beans.BoardBean;
import controls.CustomOrderController;
import exceptions.EmptyFieldException;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import utils.SceneManager;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class CustomerMakeOrdersPageView {

    SceneManager sceneManager = SceneManager.getInstance();
    CustomOrderController customOrderController;
    BoardBean skateboardBean;

    @FXML private MenuButton menuRegions;
    @FXML private Label skateboardNameLabel;
    @FXML private Label skateboardSizeLabel;
    @FXML private Label skateboardPriceLabel;
    @FXML private TextArea descriptionArea;

    @FXML private TextField streetTextField;
    @FXML private TextField provinceTextField;
    @FXML private TextField cityTextField;

    @FXML private Label regionCost;

    @FXML private Spinner<String> timeSlotSpinner;
    @FXML private TextArea commentArea;

    @FXML private Label errorLabel;

    public void setController(CustomOrderController customOrderController){
        this.customOrderController = customOrderController;
    }

    public void setSkateboardBean(BoardBean skateboardBean){
        this.skateboardBean = skateboardBean;
    }

    public void initialize() {
        loadRegionsMenu();
        String[] timeSlots = generateTimeSlots(8, 16);
        SpinnerValueFactory.ListSpinnerValueFactory<String> valueFactory =
                new SpinnerValueFactory.ListSpinnerValueFactory<>(javafx.collections.FXCollections.observableArrayList(timeSlots));

        timeSlotSpinner.setValueFactory(valueFactory);
    }

    private String[] generateTimeSlots(int startHour, int endHour) {
        String[] slots = new String[endHour - startHour];
        for (int i = startHour; i < endHour; i++) {
            String startTime = String.format("%02d:00", i);
            String endTime = String.format("%02d:00", i + 1);
            slots[i - startHour] = startTime + "-" + endTime;
        }
        return slots;
    }

    public void initData() {
        displayGeneratedSample();
    }

    public void displayGeneratedSample(){
        skateboardNameLabel.setText("Nome dello skateboard di partenza: " + skateboardBean.getName());
        skateboardSizeLabel.setText("Dimensione skateboard: " + skateboardBean.getSize());
        skateboardPriceLabel.setText("Costo totale in coins: " + skateboardBean.getPrice());
        descriptionArea.setText(skateboardBean.getDescription());
    }

    public void loadRegionsMenu(){
        List<String> regioni = Arrays.asList(
                "Abruzzo", "Basilicata", "Calabria", "Campania", "Emilia-Romagna",
                "Friuli-Venezia Giulia", "Lazio", "Liguria", "Lombardia", "Marche",
                "Molise", "Piemonte", "Puglia", "Sardegna", "Sicilia", "Toscana",
                "Trentino-Alto Adige", "Umbria", "Valle d'Aosta", "Veneto"
        );

        for (int i = 0; i < regioni.size(); i++) {
            String region = regioni.get(i);
            MenuItem item = new MenuItem(region);
            item.setOnAction(e -> {
                menuRegions.setText(region);;
            });
            menuRegions.getItems().add(item);
        }
    }

    private void updateRegionCost( int cost) {
        regionCost.setText( cost + "coins");
    }

    public void order(){
        String region = menuRegions.getText();
        String province = provinceTextField.getText();
        String city = cityTextField.getText();
        String address = streetTextField.getText();
        String comment = commentArea.getText();
        String timeSlot = timeSlotSpinner.getValue();

        try{
            if(province == null || city == null || address == null || comment == null){
                throw new EmptyFieldException("Inserisci i campi correttamente");
            }
            DeliveryDestinationBean deliveryDestinationBean = new DeliveryDestinationBean();
            deliveryDestinationBean.setRegion(region);
            deliveryDestinationBean.setProvince(province);
            deliveryDestinationBean.setCity(city);
            deliveryDestinationBean.setStreetAddress(address);

            sceneManager.loadDoneOrderPage(customOrderController,skateboardBean, deliveryDestinationBean);

        }catch(EmptyFieldException e){
            errorLabel.setText(e.getMessage());
        }
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
    public void back(){}
    public void goToCompetitionsPage(){}
    public void logOut(){}
}
