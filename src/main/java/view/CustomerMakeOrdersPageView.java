package view;

import beans.*;
import controls.CustomOrderController;
import exceptions.DataAccessException;
import exceptions.EmptyFieldException;
import exceptions.InsufficientCoinsException;
import exceptions.SessionExpiredException;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import utils.CustomerOrderView;
import utils.WindowManager;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class CustomerMakeOrdersPageView implements CustomerOrderView {

    WindowManager windowManager = WindowManager.getInstance();
    CustomOrderController customOrderController;
    BoardBean boardBean;

    @FXML private MenuButton menuRegions;


    @FXML private TextField streetTextField;
    @FXML private TextField provinceTextField;
    @FXML private TextField cityTextField;


    @FXML private Spinner<String> timeSlotSpinner;
    @FXML private TextArea commentArea;

    @FXML private Label errorLabel;

    public void setController(CustomOrderController customOrderController){
        this.customOrderController = customOrderController;
    }

    public void setBoardBean(BoardBean boardBean){
        this.boardBean = boardBean;
    }

    public void initialize() {


        loadRegionsMenu();
        String[] timeSlots = generateTimeSlots(8, 16);
        SpinnerValueFactory.ListSpinnerValueFactory<String> valueFactory =
                new SpinnerValueFactory.ListSpinnerValueFactory<>(javafx.collections.FXCollections.observableArrayList(timeSlots));

        timeSlotSpinner.setValueFactory(valueFactory);
    }

    public void orderUpdate(){
        windowManager.loadPreviousOrdersPage();
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
            item.setOnAction(e -> menuRegions.setText(region));

            menuRegions.getItems().add(item);
        }
        menuRegions.setText(regioni.get(0));
    }


    public void order() {
        String region = menuRegions.getText();
        String province = provinceTextField.getText();
        String city = cityTextField.getText();
        String address = streetTextField.getText();
        String comment = commentArea.getText();
        String timeSlot = timeSlotSpinner.getValue();

        try {
            if (province == null || city == null || address == null || comment == null) {
                throw new EmptyFieldException("Inserisci i campi correttamente");
            }

            DeliveryDestinationBean deliveryDestinationBean = new DeliveryDestinationBean();
            deliveryDestinationBean.setRegion(region);
            deliveryDestinationBean.setProvince(province);
            deliveryDestinationBean.setCity(city);
            deliveryDestinationBean.setStreetAddress(address);

            DeliveryPreferencesBean deliveryPreferencesBean = new DeliveryPreferencesBean();
            deliveryPreferencesBean.setComment(comment);
            deliveryPreferencesBean.setPreferredTimeSlot(timeSlot);

            processOrder(deliveryDestinationBean, deliveryPreferencesBean);

        } catch (EmptyFieldException e) {
            errorLabel.setText(e.getMessage());
        }
    }

    private void processOrder(DeliveryDestinationBean deliveryDestinationBean, DeliveryPreferencesBean deliveryPreferencesBean) {
        try {
            OrderSummaryBean orderSummaryBean = customOrderController.elaborateOrder(
                    windowManager.getAuthBean().getToken(),
                    deliveryDestinationBean,
                    deliveryPreferencesBean,
                    boardBean
            );
            windowManager.loadAllOrdersPage( orderSummaryBean);

        } catch (InsufficientCoinsException | DataAccessException e ) {
            errorLabel.setText(e.getMessage());
        } catch (SessionExpiredException _) {
            windowManager.closeCoordinator();
            windowManager.logOut();
        }
    }

    public void goToHomePage(){
        windowManager.closeCoordinator();
        try {
            windowManager.goToHomePage();
        } catch (IOException e) {
            errorLabel.setText(e.getMessage());
        }
    }

    public void goToTricksPage(){
        try {
            windowManager.goToLearn();
        } catch (IOException e) {
            errorLabel.setText(e.getMessage());
        }
    }
    public void goToCompetitionsPage(){
        try{
            windowManager.goToCustomerCompetitions();
        } catch (IOException e) {
            errorLabel.setText(e.getMessage());
        }
    }
    public void logOut() {
        windowManager.closeCoordinator();
        windowManager.logOut();
    }
}
