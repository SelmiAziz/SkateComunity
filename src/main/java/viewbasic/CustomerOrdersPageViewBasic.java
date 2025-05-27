package viewbasic;

import beans.*;
import controls.CustomOrderController;
import exceptions.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import utils.CustomerOrderView;
import utils.WindowManagerBasic;
import java.io.IOException;
import java.util.*;

public class CustomerOrdersPageViewBasic implements CustomerOrderView {
    private CustomOrderController customOrderController;
    private WindowManagerBasic windowManagerBasic = WindowManagerBasic.getInstance();

    private final Map<Integer, String> boardIdMap = new HashMap<>();
    private final Map<Integer, String> orderIdMap = new HashMap<>();


    @FXML private ChoiceBox<String> choicePage;
    @FXML private Pane designControlPane;
    @FXML private Pane orderControlPane;
    @FXML private Pane notesPane;
    @FXML private Pane boardPane;
    @FXML private Pane orderSubmitPane;
    @FXML private ListView<String> allListView;
    @FXML private ListView<String> notesListView;

    @FXML private ComboBox<Integer> boardsComboBox;
    @FXML private ComboBox<Integer> ordersComboBox;


    @FXML private TextField regionField;
    @FXML private TextField provinceField;
    @FXML private TextField cityField;

    @FXML private TextField addressField;
    @FXML private TextField addressNumberField;

    @FXML private ComboBox<String> comboSlot;
    @FXML private TextArea preferenceArea;

    BoardProfileBean boardProfileBean ;

    BoardBean boardBean;
    OrderSummaryBean orderSummaryBean;


    @FXML private Button orderButton;
    @FXML private Button boardButton;

    @FXML private Button availableButton;
    @FXML private Button designButton;

    @FXML private ComboBox<String> noseCombo;
    @FXML private ComboBox<String> tailCombo;
    @FXML private ComboBox<String> warrantyCombo;
    @FXML private ComboBox<String> pilesCombo;
    @FXML private ComboBox<String> gripCombo;

    @FXML private TextArea resultArea;



    @FXML private Label errorLabel;

    private String boardTypology;


    private static final String COLOR1 = "1ABC9C";
    private static final String COLOR2 = "B0B0B0";
    public void initialize(){
        initGripCombo();
        initWarrantyCombo();
        initPilesCombo();
        initNoseCombo();
        initTailCombo();
        populatePageChoice();
        resultArea.setEditable(false);
        confStart();
        initSlotCombo();
        setButtonStyle(availableButton, COLOR1);
        setButtonStyle(designButton, COLOR2);
        setButtonStyle(boardButton, COLOR1);
        setButtonStyle(orderButton, COLOR2);
    }

    private void initSlotCombo() {
        List<String> slotValues = new ArrayList<>();
        for (int hour = 8; hour <= 20; hour++) {
            slotValues.add(String.format("%02d:00", hour));
        }
        comboSlot.setItems(FXCollections.observableArrayList(slotValues));
        comboSlot.setValue("10:00");
    }



    private void initGripCombo() {
        List<String> gripValues = new ArrayList<>();
        for (double val = 0.40; val <= 0.75; val += 0.01) {
            gripValues.add(String.format(Locale.US, "%.2f", val));
        }
        gripCombo.setItems(FXCollections.observableArrayList(gripValues));
        gripCombo.setValue("0.00");
    }

    private void initWarrantyCombo() {
        warrantyCombo.getItems().clear();
        for (int i = 0; i <= 12; i++) {
            warrantyCombo.getItems().add(String.valueOf(i));
        }
        warrantyCombo.setValue("0");
    }

    private void initPilesCombo() {
        pilesCombo.getItems().clear();
        for (int i = 0; i <= 2; i++) {
            pilesCombo.getItems().add(String.valueOf(i));
        }
        pilesCombo.setValue("0");
    }

    private void initNoseCombo() {
        noseCombo.getItems().clear();
        for (int i = 0; i <= 50; i++) {
            noseCombo.getItems().add(String.valueOf(i));
        }
        noseCombo.setValue("0");
    }

    private void initTailCombo() {
        tailCombo.getItems().clear();
        for (int i = 0; i <= 50; i++) {
            tailCombo.getItems().add(String.valueOf(i));
        }
        tailCombo.setValue("0");
    }

    public void setController(CustomOrderController customOrderController){
        this.customOrderController = customOrderController;
    }


    public void initializeAfter(){
        displayAvailableBoards();
        confStart();
    }

    public void confStart(){
        orderSubmitPane.setVisible(false);
        boardPane.setVisible(false);
        notesPane.setVisible(false);
        orderControlPane.setVisible(false);
        designControlPane.setVisible(true);
    }

    private void setButtonStyle(Button button, String colorHex) {
        button.setStyle("-fx-background-color: " + colorHex + "; -fx-text-fill: white;");
    }

    public void saveBoard(){
        try {
            boardBean = customOrderController.saveCreatedCustomizedBoard(windowManagerBasic.getAuthBean().getToken(), boardProfileBean);
            boardPane.setVisible(false);
            orderSubmitPane.setVisible(true);
        }catch(SessionExpiredException _ ){
            windowManagerBasic.cleanOrderPage();
            windowManagerBasic.logOut();
        }catch(DataAccessException e){
            errorLabel.setText(e.getMessage());
        }
    }


    public String formatAddress(String street, String number) {
        return street.trim() + " " + number.trim();
    }


    public void validateNumber(String number) throws WrongFormatException {
        try {
            Integer.parseInt(number);
        } catch (NumberFormatException _) {
            throw new WrongFormatException("Address number has to be a number, please enter a number!!");
        }
    }

    public void orderBoard() {
        try {
            validateInputFields();
            DeliveryPreferencesBean deliveryPreferencesBean = prepareDeliveryPreferences();
            DeliveryDestinationBean deliveryDestinationBean = prepareDeliveryDestination();

            confStart();

            elaborateOrder(deliveryDestinationBean, deliveryPreferencesBean);

        } catch (EmptyFieldException | InvalidRegionException e) {
            errorLabel.setText(e.getMessage());
        }
    }

    private void validateInputFields() throws EmptyFieldException, InvalidRegionException {
        String comment = preferenceArea.getText();
        String region = regionField.getText();
        String province = provinceField.getText();
        String city = cityField.getText();
        String streetAddress = addressField.getText();
        String number = addressNumberField.getText();

        if (comment == null || region == null || province == null || city == null || streetAddress == null || number == null) {
            throw new EmptyFieldException("EmptyField");
        }

        validateRegion(region);
        validateNumber(number);
    }

    private DeliveryPreferencesBean prepareDeliveryPreferences() {
        DeliveryPreferencesBean deliveryPreferencesBean = new DeliveryPreferencesBean();
        deliveryPreferencesBean.setComment(preferenceArea.getText());
        deliveryPreferencesBean.setPreferredTimeSlot(comboSlot.getValue());
        return deliveryPreferencesBean;
    }

    private DeliveryDestinationBean prepareDeliveryDestination() {
        DeliveryDestinationBean deliveryDestinationBean = new DeliveryDestinationBean();
        deliveryDestinationBean.setCity(cityField.getText());
        deliveryDestinationBean.setProvince(provinceField.getText());
        deliveryDestinationBean.setRegion(formatRegion(regionField.getText()));
        deliveryDestinationBean.setStreetAddress(formatAddress(addressField.getText(), addressNumberField.getText()));
        return deliveryDestinationBean;
    }

    private void elaborateOrder(DeliveryDestinationBean deliveryDestinationBean, DeliveryPreferencesBean deliveryPreferencesBean) {
        try {
            orderSummaryBean = customOrderController.elaborateOrder(
                    windowManagerBasic.getAuthBean().getToken(),
                    deliveryDestinationBean,
                    deliveryPreferencesBean,
                    boardBean
            );
        } catch (InsufficientCoinsException | DataAccessException e) {
            errorLabel.setText(e.getMessage());
        } catch (SessionExpiredException _) {
            windowManagerBasic.cleanOrderPage();
            windowManagerBasic.logOut();
        }
    }

    public void validateRegion(String regione) throws InvalidRegionException {
        Set<String> regioniValide = Set.of(
                "Abruzzo", "Basilicata", "Calabria", "Campania", "Emilia-Romagna",
                "Friuli-Venezia Giulia", "Lazio", "Liguria", "Lombardia", "Marche",
                "Molise", "Piemonte", "Puglia", "Sardegna", "Sicilia",
                "Toscana", "Trentino-Alto Adige", "Umbria", "Valle d'Aosta", "Veneto"
        );

        String regioneFormattata = formatRegion(regione.trim().toLowerCase());

        if (!regioniValide.contains(regioneFormattata)) {
            throw new InvalidRegionException();
        }
    }

    private String formatRegion(String input) {
        String[] words = input.split("[-\\s]");
        StringJoiner joiner = new StringJoiner(" ");

        for (String word : words) {
            if (!word.isEmpty()) {
                joiner.add(Character.toUpperCase(word.charAt(0)) + word.substring(1).toLowerCase());
            }
        }

        return joiner.toString();
    }


    private void populatePageChoice() {
        List<String> list = Arrays.asList( "Learn", "Competitions", "Log Out");
        ObservableList<String> pages = FXCollections.observableArrayList(list);
        choicePage.setItems(pages);
        choicePage.setValue("Board");
    }

    @Override
    public void orderUpdate() {
        confStart();
        orderConf();
        displayOrders();
    }

    public void changePage(){
        String page = choicePage.getValue();
        if(page.equals("Competitions")){
            try {
                windowManagerBasic.goToCustomerCompetitions();
                windowManagerBasic.cleanOrderPage();
            } catch(IOException e){
                errorLabel.setText(e.getMessage());
            }
        }else if(page.equals("Learn")){
            try{
                windowManagerBasic.goToLearn();
                windowManagerBasic.cleanOrderPage();
            }catch(IOException e){
                errorLabel.setText(e.getMessage());
            }
        }else if(page.equals("Log Out")){
            windowManagerBasic.cleanOrderPage();
                windowManagerBasic.logOut();

        }
    }

    public void boardConf(){
        setButtonStyle(boardButton,COLOR1 );
        setButtonStyle(orderButton, COLOR2);
        confStart();
        displayAvailableBoards();
    }

    public void process(){
        double grip = Double.parseDouble(gripCombo.getValue());
        double nose = Double.parseDouble(noseCombo.getValue());
        double tail = Double.parseDouble(tailCombo.getValue());
        int warranty = Integer.parseInt(warrantyCombo.getValue());
        int piles = Integer.parseInt(pilesCombo.getValue());


        CustomBoardBean customBoardBean = new CustomBoardBean();

        customBoardBean.setName(boardIdMap.get(boardsComboBox.getValue()));
        if(nose == 0){
            customBoardBean.setUseConcaveNose(false);
        }else{
            customBoardBean.setUseConcaveNose(true);
            customBoardBean.setConcaveNose(nose);
        }

        if(tail == 0){
            customBoardBean.setUseConcaveTail(false);
        }else{
            customBoardBean.setUseConcaveTail(true);
            customBoardBean.setConcaveTail(tail);
        }

        if(grip == 0){
            customBoardBean.setUseGripTexture(false);
        }else{
            customBoardBean.setUseGripTexture(false);
            customBoardBean.setGripTexture(grip);
        }

        if(warranty == 0){
            customBoardBean.setUseWarrantyMonths(false);
        }else{
            customBoardBean.setUseWarrantyMonths(true);
            customBoardBean.setWarrantyMonths(warranty);
        }

        if(piles == 0){
            customBoardBean.setUseExtraPiles(false);
        }else{
            customBoardBean.setUseExtraPiles(true);
            customBoardBean.setExtraPiles(piles);
        }
       try{
           boardProfileBean = customOrderController.generateCustomBoard(windowManagerBasic.getAuthBean().getToken(),customBoardBean);

           resultArea.setText(
                   "Price: " + boardProfileBean.getPrice() + "\n\n" +
                           boardProfileBean.getDescription()
           );
       }catch(SessionExpiredException _){
          windowManagerBasic.cleanOrderPage();
          windowManagerBasic.logOut();
       }catch (DataAccessException e){
           errorLabel.setText(e.getMessage());
       }
    }


    public void orderConf(){
        setButtonStyle(orderButton,COLOR1);
        setButtonStyle(boardButton, COLOR2);
        notesPane.setVisible(true);
        designControlPane.setVisible(false);
        orderControlPane.setVisible(true);
        displayOrders();
    }

    public void displayOrders() {
        try {
            List<OrderSummaryBean> orders = customOrderController.getSubmittedOrders(windowManagerBasic.getAuthBean().getToken());
            ObservableList<String> items = FXCollections.observableArrayList();
            orderIdMap.clear();
            int index = 1;

            for (OrderSummaryBean order : orders) {
                String displayText = String.format(
                        "[%d] %s - %s | €%d | Status: %s | Delivery: %s | Destination: %s, %s, %s, %s",
                        index,
                        order.getNameBoard(),
                        order.getDescriptionBoard(),
                        order.getTotalCost(),
                        order.getStatus(),
                        order.getDeliveryDate(),
                        order.getStreetAddersDestination(),
                        order.getCityDestination(),
                        order.getProvinceDestination(),
                        order.getRegionDestination()
                );

                items.add(displayText);
                orderIdMap.put(index, order.getId());
                index++;
            }

            allListView.setItems(items);

            ObservableList<Integer> comboItems = FXCollections.observableArrayList();
            for (int i = 1; i <= orders.size(); i++) {
                comboItems.add(i);
            }
            ordersComboBox.setItems(comboItems);

            if (!comboItems.isEmpty()) {
                ordersComboBox.getSelectionModel().selectFirst();
            }

        } catch (SessionExpiredException _) {
            windowManagerBasic.cleanOrderPage();
            windowManagerBasic.logOut();
        }catch(DataAccessException e){
            errorLabel.setText(e.getMessage());
        }
    }


    public void displayDesignedBoards() {
        try {
            List<BoardProfileBean> boards = customOrderController.getCustomizedBoards(windowManagerBasic.getAuthBean().getToken());
            updateBoardList(boards, designButton, availableButton, false);
            boardTypology = "designed";
        }catch(SessionExpiredException _){
            windowManagerBasic.cleanOrderPage();
            windowManagerBasic.logOut();
        }catch(DataAccessException e){
            errorLabel.setText(e.getMessage());
        }
    }

    public void displayAvailableBoards() {
        try {
            List<BoardProfileBean> boards = customOrderController.getBoardSamples(windowManagerBasic.getAuthBean().getToken());
            updateBoardList(boards, availableButton, designButton, true);
            boardTypology = "available";
        }catch(SessionExpiredException _){
            windowManagerBasic.cleanOrderPage();
            windowManagerBasic.logOut();
        }catch(DataAccessException e){
            errorLabel.setText(e.getMessage());
        }
    }

    private void updateBoardList(List<BoardProfileBean> boards, Button active, Button inactive, boolean saveNameInsteadOfId) {
        setButtonStyle(active, COLOR1);
        setButtonStyle(inactive, COLOR2);

        ObservableList<String> items = FXCollections.observableArrayList();
        boardIdMap.clear();

        int index = 1;
        for (BoardProfileBean board : boards) {
            String displayText = String.format("[%d] %s - %s | €%d | Size: %s",
                    index,
                    board.getName(),
                    board.getDescription(),
                    board.getPrice(),
                    board.getSize()
            );
            items.add(displayText);

            if (saveNameInsteadOfId) {
                boardIdMap.put(index, board.getName());
            } else {
                boardIdMap.put(index, board.getId());
            }

            index++;
        }

        allListView.setItems(items);

        ObservableList<Integer> comboItems = FXCollections.observableArrayList();
        for (int i = 1; i <= boards.size(); i++) {
            comboItems.add(i);
        }
        boardsComboBox.setItems(comboItems);


        if (!comboItems.isEmpty()) {
            boardsComboBox.getSelectionModel().selectFirst();
        }
    }


    public void displayProgressNotes() {
        try {
            ObservableList<String> items = FXCollections.observableArrayList();
            String orderId = orderIdMap.get(ordersComboBox.getValue());
            OrderBean orderBean = new OrderBean();
            orderBean.setId(orderId);
            List<ProgressNoteBean> progressNoteBeanList = customOrderController.getProgressNotesOrder(windowManagerBasic.getAuthBean().getToken(), orderBean);

            for (ProgressNoteBean progressNoteBean : progressNoteBeanList) {
                String displayText = String.format("%s | %s",
                        progressNoteBean.getDate(),
                        progressNoteBean.getComment()
                );
                items.add(displayText);
            }

            notesListView.setItems(items);
        }catch(SessionExpiredException _){
            windowManagerBasic.logOut();
        }catch(DataAccessException e){
            errorLabel.setText(e.getMessage());
        }
    }



    public void displayBoard(){
        if(boardTypology.equals("designed")){
            boardBean = new BoardBean();
            boardBean.setId(boardIdMap.get(boardsComboBox.getValue()));
            orderSubmitPane.setVisible(true);
        }else{
            boardPane.setVisible(true);
        }

    }




}
