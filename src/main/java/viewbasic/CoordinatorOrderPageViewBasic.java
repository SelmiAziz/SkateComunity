package viewbasic;

import beans.OrderBean;
import beans.OrderSummaryBean;
import beans.ProgressNoteBean;
import controls.CustomOrderController;
import exceptions.EmptyFieldException;
import exceptions.WrongFormatException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import utils.CoordinatorOrderView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CoordinatorOrderPageViewBasic implements CoordinatorOrderView {
    private CustomOrderController customOrderController;
    private DateValidatorFormatter dateValidatorFormatter;

    private final Map<Integer, OrderSummaryBean> ordersMap = new HashMap<>();

    OrderBean orderBean;

    @FXML private Pane orderApprovePane;
    @FXML private Pane postNotePane;
    @FXML private TextArea notesArea;
    @FXML private TextField dayField;
    @FXML private TextField monthField;
    @FXML private TextField yearField;
    @FXML private ListView<String> ordersList;
    @FXML private ComboBox<Integer> ordersComboBox;
    @FXML private Label errorLabel;

    public void initialize(){
        confStart();
    }

    public void confStart() {
        orderApprovePane.setVisible(false);
        postNotePane.setVisible(false);
    }

    public void rejectOrder() {
        customOrderController.acceptCustomOrder(orderBean, false);
        confStart();
    }

    public void acceptOrder() {
        customOrderController.acceptCustomOrder(orderBean, true);
        confStart();
    }

    @Override
    public void newOrder() {
        displayOrders();
    }

    public void setController(CustomOrderController customOrderController) {
        this.customOrderController = customOrderController;
    }

    public void displayOrders() {
        List<OrderSummaryBean> orders = customOrderController.getAllOrders();
        ObservableList<String> items = FXCollections.observableArrayList();
        ordersMap.clear();
        int index = 1;

        for (OrderSummaryBean order : orders) {
            String displayText = String.format(
                    "[%d] %s - %s | €%d | Status: %s | Delivery: %s | Destination: %s, %s, %s, %s | Created: %s | Est. Days: %d",
                    index,
                    order.getNameBoard(),
                    order.getDescriptionBoard(),
                    order.getTotalCost(),
                    order.getStatus(),
                    order.getDeliveryDate(),
                    order.getStreetAddersDestination(),
                    order.getCityDestination(),
                    order.getProvinceDestination(),
                    order.getRegionDestination(),
                    order.getCreationDate(),
                    order.getEstimatedDays()
            );

            items.add(displayText);
            ordersMap.put(index, order);
            index++;
        }

        ordersList.setItems(items);

        ObservableList<Integer> comboItems = FXCollections.observableArrayList();
        for (int i = 1; i < index; i++) {
            comboItems.add(i);
        }

        ordersComboBox.setItems(comboItems);

        if (!comboItems.isEmpty()) {
            ordersComboBox.getSelectionModel().selectFirst();
        }
    }

    public void postNote() {
        String comment = notesArea.getText();
        String day = dayField.getText();
        String month = monthField.getText();
        String year = yearField.getText();

        try {
            if (comment == null || day == null || month == null || year == null) {
                throw new EmptyFieldException("Compilare tutti i campi!!");
            }

            String date = dateValidatorFormatter.formatValidateDate(day, month, year);

            ProgressNoteBean progressNoteBean = new ProgressNoteBean();
            progressNoteBean.setComment(comment);
            progressNoteBean.setDate(date);
            customOrderController.writeNote(orderBean, progressNoteBean);

        } catch (EmptyFieldException | WrongFormatException e) {
            errorLabel.setText(e.getMessage());
        }finally{
            confStart();
        }
    }

    public void completeOrder() {
        String comment = notesArea.getText();
        String day = dayField.getText();
        String month = monthField.getText();
        String year = yearField.getText();

        try {
            if (comment == null || day == null || month == null || year == null) {
                throw new EmptyFieldException("Compilare tutti i campi!!");
            }

            String date = dateValidatorFormatter.formatValidateDate(day, month, year);

            ProgressNoteBean progressNoteBean = new ProgressNoteBean();
            progressNoteBean.setComment(comment);
            progressNoteBean.setDate(date);
            customOrderController.completeOrder(orderBean, progressNoteBean);

        } catch (EmptyFieldException | WrongFormatException e) {
            errorLabel.setText(e.getMessage());
        }finally{
            confStart();
        }
    }

    public void selectOrder() {
        Integer selectedIndex = ordersComboBox.getValue();

        OrderSummaryBean selected = ordersMap.get(selectedIndex);


        orderBean = new OrderBean();
        orderBean.setId(selected.getId());



        if(selected.getStatus().equals("Requested")){
            orderApprovePane.setVisible(true);
            postNotePane.setVisible(false);
        }else{
            postNotePane.setVisible(true);
            orderApprovePane.setVisible(false);
        }
    }
}


