package controls;

import beans.*;
import dao.*;
import dao.patternAbstractFactory.DaoFactory;

import model.*;
import model.decorator.*;
import utils.DateConverter;
import utils.SessionManager;
import view.CoordinatorOrderPageView;
import view.CustomerAllOrdersPageView;

import java.util.ArrayList;
import java.util.List;

public class CustomOrderController {
    private final BoardDao boardDao = DaoFactory.getInstance().createBoardDao();
    private final CustomOrderDao customOrderDao = DaoFactory.getInstance().createCustomOrderDao();
    private final CustomerDao customerDao = DaoFactory.getInstance().createCostumerDao();
    private final ProgressNoteDao progressNoteDao = DaoFactory.getInstance().createProgressNoteDao();
    private CustomerAllOrdersPageView customerAllOrdersPageView;
    private CoordinatorOrderPageView  coordinatorOrderPageView;
    private final DateConverter dateConverter = new DateConverter();
    private final PaymentController paymentController = new PaymentController();

    public void setCoordinatorOrderPageView(CoordinatorOrderPageView coordinatorOrderPageView) {
        this.coordinatorOrderPageView = coordinatorOrderPageView;
    }

    public void setCustomAllOrdersPageView(CustomerAllOrdersPageView customerAllOrdersPageView) {
        this.customerAllOrdersPageView = customerAllOrdersPageView;
    }

    public List<CustomizedBoardBean> getBoardSamples(){
        List<CustomizedBoardBean> boardBeanList = new ArrayList<>();
        List<Board> boardList = boardDao.selectAvailableBoards();
        for(Board board : boardList){
            CustomizedBoardBean boardBean = new CustomizedBoardBean();
            boardBean.setName(board.name());
            boardBean.setDescription(board.description());
            boardBean.setPrice(board.price());
            boardBean.setSize(board.size());
            boardBeanList.add(boardBean);
        }
        return boardBeanList;
    }

    public CustomizedBoardBean generateCustomBoard(CustomBoardBean customBoardBean){
        Board baseBoard = boardDao.selectBoardByName(customBoardBean.getName());

        GripTextureDecorator grip = new GripTextureDecorator(baseBoard, customBoardBean.getGripTexture());
        NoseConcaveDecorator nose = new NoseConcaveDecorator(grip, customBoardBean.getConcaveNose());
        TailConcaveDecorator tail = new TailConcaveDecorator(nose, customBoardBean.getConcaveTail());
        WarrantyDecorator warranty = new WarrantyDecorator(tail, customBoardBean.getWarrantyMonths());
        ExtraPilesDecorator piles = new ExtraPilesDecorator(warranty, customBoardBean.getExtraPiles());

        CustomizedBoardBean boardBean = new CustomizedBoardBean();
        boardBean.setName(baseBoard.name());
        boardBean.setDescription(piles.description());
        boardBean.setSize(baseBoard.size());
        boardBean.setPrice(piles.price());

        return boardBean;
    }

    public BoardBean saveCreatedCustomizedBoard(CustomizedBoardBean customizedBoardBean){
        Customer customer = customerDao.selectCustomerByUsername(SessionManager.getInstance().getSession().getUsername());
        Board board = new BoardBase(customizedBoardBean.getName(), customizedBoardBean.getDescription(), customizedBoardBean.getSize(), customizedBoardBean.getPrice());
        customer.addDesignBoard(board);
        boardDao.addBoard(board, customer.getUsername());
        BoardBean boardBean = new BoardBean();
        boardBean.setId(board.boardId());
        return boardBean;
    }


    public List<CustomizedBoardBean> getCustomizedBoards(){
        Customer customer = customerDao.selectCustomerByUsername(SessionManager.getInstance().getSession().getUsername());
        List<CustomizedBoardBean> boardBeanList = new ArrayList<>();
        for(Board board : customer.customizedBoards()){
            CustomizedBoardBean boardBean = new CustomizedBoardBean();
            boardBean.setId(board.boardId());
            boardBean.setDescription(board.description());
            boardBean.setName(board.name());
            boardBean.setSize(board.size());
            boardBean.setPrice(board.price());
            boardBeanList.add(boardBean);
        }
        return boardBeanList;
    }

    protected void notifyOrderCoordinator() {
        coordinatorOrderPageView.newOrder();
    }

    protected void notifyCustomer(){
        customerAllOrdersPageView.orderUpdate();
    }

    public void acceptCustomOrder(OrderBean customOrderBean, boolean accept){
        Order customOrder = customOrderDao.selectCustomOrderById(customOrderBean.getId());
        if(accept){
            customOrder.setOrderStatus(OrderStatus.PROCESSING);
        }else{
            customOrder.setOrderStatus(OrderStatus.REJECTED);
            int coinsRefunded = customOrder.totalCost();
            Wallet  wallet = customOrder.getCustomer().getWallet();
            wallet.depositCoins(coinsRefunded);
        }
        customOrderDao.updateCustomOrder(customOrder);
        notifyCustomer();
    }


    public void writeNote(OrderBean customOrderBean, ProgressNoteBean progressNoteBean){
        ProgressNote progressNote = new ProgressNote(progressNoteBean.getComment(),dateConverter.stringToLocalDate(progressNoteBean.getDate()));

        Order customOrder = customOrderDao.selectCustomOrderById(customOrderBean.getId());
        customOrder.addProgressNoteOrder(progressNote);
        progressNoteDao.saveProgressNote(progressNote, customOrder.getId());
        notifyCustomer();
    }

    public void completeOrder(OrderBean customOrderBean, ProgressNoteBean progressNoteBean){
        ProgressNote progressNote = new ProgressNote(progressNoteBean.getComment(),dateConverter.stringToLocalDate( progressNoteBean.getDate()));
        Order customOrder = customOrderDao.selectCustomOrderById(customOrderBean.getId());
        customOrder.addProgressNoteOrder(progressNote);
        progressNoteDao.saveProgressNote(progressNote, customOrder.getId());
        customOrder.setOrderStatus(OrderStatus.COMPLETED);
        customOrderDao.updateCustomOrder(customOrder);
        notifyCustomer();
    }

    public OrderSummaryBean elaborateOrder(DeliveryDestinationBean deliveryDestinationBean, DeliveryPreferencesBean deliveryPreferencesBean, BoardBean boardBean){

        DeliveryDestination deliveryDestination = new DeliveryDestination(Region.fromString(deliveryDestinationBean.getRegion()),
                deliveryDestinationBean.getProvince(),
                deliveryDestinationBean.getCity(),
                deliveryDestinationBean.getStreetAddress());

        Board board = boardDao.selectBoardById(boardBean.getId());


        DeliveryPreferences deliveryPreferences = new DeliveryPreferences(deliveryPreferencesBean.getComment(), deliveryPreferencesBean.getPreferredTimeSlot());

        Customer customer = customerDao.selectCustomerByUsername(SessionManager.getInstance().getSession().getUsername());

        Order customOrder = new Order(deliveryDestination,deliveryPreferences, board);
        customOrder.setCustomer(customer);
        customer.addSubmittedOrder(customOrder);

        int costCoins = customOrder.totalCost();
        Wallet wallet = customer.getWallet();
        paymentController.payWithCoins(wallet, costCoins);

        this.customOrderDao.saveCustomOrder(customOrder);
        notifyOrderCoordinator();

        OrderSummaryBean customOrderSummaryBean = new OrderSummaryBean();
        customOrderSummaryBean.setId(customOrder.getId());
        customOrderSummaryBean.setCreationDate(dateConverter.localDateToString(customOrder.creationDate()));
        customOrderSummaryBean.setDescriptionBoard(customOrder.getBoard().description());
        customOrderSummaryBean.setRegionDestination(customOrder.getDeliveryDestination().getRegion().toString());
        customOrderSummaryBean.setProvinceDestination(customOrder.getDeliveryDestination().getProvince());
        customOrderSummaryBean.setCityDestination(customOrder.getDeliveryDestination().getCity());
        customOrderSummaryBean.setStreetAddersDestination(customOrder.getDeliveryDestination().getStreetAddress());
        customOrderSummaryBean.setEstimatedDays(customOrder.getDeliveryDestination().estimatedDeliveryDays());
        customOrderSummaryBean.setStatus(customOrder.getOrderStatus().toString());
        customOrderSummaryBean.setNameBoard(customOrder.getBoard().name());
        customOrderSummaryBean.setTotalCost(customOrder.totalCost());
        return customOrderSummaryBean;
    }


    public List<OrderSummaryBean> getAllOrders(){
       List<Order> orderList = customOrderDao.selectAllOpenOrder();
       List<OrderSummaryBean> customOrderBeanList = new ArrayList<>();
       for(Order order:orderList){
           OrderSummaryBean orderSummaryBean = new OrderSummaryBean();
           orderSummaryBean.setId(order.getId());
           orderSummaryBean.setCreationDate(dateConverter.localDateToString(order.creationDate()));
           orderSummaryBean.setDescriptionBoard(order.getBoard().description());
           orderSummaryBean.setStatus(order.getOrderStatus().toString());
           orderSummaryBean.setRegionDestination(order.getDeliveryDestination().getRegion().toString());
           orderSummaryBean.setProvinceDestination(order.getDeliveryDestination().getProvince());
           orderSummaryBean.setCityDestination(order.getDeliveryDestination().getCity());
           orderSummaryBean.setStreetAddersDestination(order.getDeliveryDestination().getStreetAddress());
           customOrderBeanList.add(orderSummaryBean);
       }
        return customOrderBeanList;
    }


    public List<OrderSummaryBean> getOrdersSubmitted(){
        Customer customer = customerDao.selectCustomerByUsername(SessionManager.getInstance().getSession().getUsername());
        List<OrderSummaryBean> customOrderBeanList = new ArrayList<>();
        for(Order order: customer.customOrdersSubmitted()){
            OrderSummaryBean orderSummaryBean = new OrderSummaryBean();
            orderSummaryBean.setId(order.getId());
            orderSummaryBean.setCreationDate(dateConverter.localDateToString(order.creationDate()));
            orderSummaryBean.setDeliveryDate(dateConverter.localDateToString(order.deliveryDate()));
            orderSummaryBean.setStatus(order.getOrderStatus().toString());
            orderSummaryBean.setTotalCost(order.totalCost());
            orderSummaryBean.setDescriptionBoard(order.getBoard().description());
            orderSummaryBean.setRegionDestination(order.getDeliveryDestination().getRegion().toString());
            orderSummaryBean.setProvinceDestination(order.getDeliveryDestination().getProvince());
            orderSummaryBean.setCityDestination(order.getDeliveryDestination().getCity());
            orderSummaryBean.setStreetAddersDestination(order.getDeliveryDestination().getStreetAddress());
            customOrderBeanList.add(orderSummaryBean);
        }
        return customOrderBeanList;
    }


    public List<OrderSummaryBean> getOrdersAcquired(){
        Customer customer = customerDao.selectCustomerByUsername(SessionManager.getInstance().getSession().getUsername());
        List<OrderSummaryBean> customOrderBeanList = new ArrayList<>();
        for(Order order: customer.customOrdersAcquired()){
            OrderSummaryBean orderSummaryBean = new OrderSummaryBean();
            orderSummaryBean.setId(order.getId());
            orderSummaryBean.setCreationDate(dateConverter.localDateToString(order.creationDate()));
            orderSummaryBean.setDeliveryDate(dateConverter.localDateToString(order.deliveryDate()));
            orderSummaryBean.setStatus(order.getOrderStatus().toString());
            orderSummaryBean.setRegionDestination(order.getDeliveryDestination().getRegion().toString());
            orderSummaryBean.setProvinceDestination(order.getDeliveryDestination().getProvince());
            orderSummaryBean.setCityDestination(order.getDeliveryDestination().getCity());
            orderSummaryBean.setStreetAddersDestination(order.getDeliveryDestination().getStreetAddress());
            orderSummaryBean.setDescriptionBoard(order.getBoard().description());
            customOrderBeanList.add(orderSummaryBean);
        }
        return customOrderBeanList;
    }




    public List<ProgressNoteBean> progressNoteBeanList(OrderSummaryBean customOrderBean){
        Order order = customOrderDao.selectCustomOrderById(customOrderBean.getId());
        List<ProgressNote> progressNoteList = order.progressDetails();
        List<ProgressNoteBean> progressNoteBeanList = new ArrayList<>();
        for(ProgressNote progressNote: progressNoteList){
            ProgressNoteBean progressNoteBean = new ProgressNoteBean();
            progressNoteBean.setComment(progressNote.getComment());
            progressNoteBean.setDate(dateConverter.localDateToString(progressNote.getDate()));
            progressNoteBeanList.add(progressNoteBean);
        }
        return progressNoteBeanList;
    }

    public WalletBean walletDetails(){
        Customer customer = customerDao.selectCustomerByUsername(SessionManager.getInstance().getSession().getUsername());
        Wallet wallet = customer.getWallet();
        WalletBean walletBean = new WalletBean();
        walletBean.setBalance(wallet.getBalance());
        return walletBean;
    }

    public CustomerBean getInfoCustomerOrder(OrderBean orderBean){
        Order order = customOrderDao.selectCustomOrderById(orderBean.getId());
        Customer customer = order.getCustomer();
        CustomerBean customerBean = new CustomerBean();
        customerBean.setSkillLevel(customer.getSkaterLevel().toString());
        customerBean.setUsername(customer.getUsername());
        return customerBean;
    }
}

