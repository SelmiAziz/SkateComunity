package controls;

import beans.*;
import dao.*;
import dao.patternAbstractFactory.DaoFactory;

import model.*;
import model.decorator.*;
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
    private final PaymentController paymentController = new PaymentController();

    public void setCoordinatorOrderPageView(CoordinatorOrderPageView coordinatorOrderPageView) {
        this.coordinatorOrderPageView = coordinatorOrderPageView;
    }

    public void setCustomAllOrdersPageView(CustomerAllOrdersPageView customerAllOrdersPageView) {
        this.customerAllOrdersPageView = customerAllOrdersPageView;
    }

    public List<BoardBean> getBoardSamples(){
        List<BoardBean> boardBeanList = new ArrayList<>();
        List<Board> boardList = boardDao.selectAvailableBoards();
        for(Board board : boardList){
            BoardBean boardBean = new BoardBean();
            boardBean.setName(board.name());
            boardBean.setDescription(board.description());
            boardBean.setPrice(board.price());
            boardBean.setSize(board.size());
            boardBeanList.add(boardBean);
        }
        return boardBeanList;
    }

    public BoardBean generateCustomBoard(CustomBoardBean customBoardBean){
        Board baseBoard = boardDao.selectBoardByName(customBoardBean.getName());

        GripTextureDecorator grip = new GripTextureDecorator(baseBoard, customBoardBean.getGripTexture());
        NoseConcaveDecorator nose = new NoseConcaveDecorator(grip, customBoardBean.getConcaveNose());
        TailConcaveDecorator tail = new TailConcaveDecorator(nose, customBoardBean.getConcaveTail());
        WarrantyDecorator warranty = new WarrantyDecorator(tail, customBoardBean.getWarrantyMonths());
        ExtraPilesDecorator piles = new ExtraPilesDecorator(warranty, customBoardBean.getExtraPiles());

        BoardBean boardBean = new BoardBean();
        boardBean.setId(baseBoard.boardId());
        boardBean.setName(baseBoard.name());
        boardBean.setDescription(piles.description());
        boardBean.setSize(baseBoard.size());
        boardBean.setPrice(piles.price());

        return boardBean;
    }

    public void saveCreatedCustomizedBoard(BoardBean boardBean){
        Customer customer = customerDao.selectCustomerByUsername(SessionManager.getInstance().getSession().getUsername());
        Board board = new BoardBase(boardBean.getId(),boardBean.getName(), boardBean.getDescription(), boardBean.getSize(), boardBean.getPrice());
        customer.addDesignBoard(board);
        boardDao.addBoard(board, customer.getUsername());
    }


    public List<BoardBean> getCustomizedBoards(){
        Customer customer = customerDao.selectCustomerByUsername(SessionManager.getInstance().getSession().getUsername());
        List<BoardBean> boardBeanList = new ArrayList<>();
        for(Board board : customer.customizedBoards()){
            BoardBean boardBean = new BoardBean();
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

    public void acceptCustomOrder(CustomOrderBean customOrderBean, boolean accept){
        CustomOrder customOrder = customOrderDao.selectCustomOrderById(customOrderBean.getId());
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


    public void writeNote( CustomOrderBean customOrderBean, ProgressNoteBean progressNoteBean){
        ProgressNote progressNote = new ProgressNote(progressNoteBean.getComment(), progressNoteBean.getDate());

        CustomOrder customOrder = customOrderDao.selectCustomOrderById(customOrderBean.getId());
        customOrder.addProgressNoteOrder(progressNote);
        progressNoteDao.saveProgressNote(progressNote, customOrder.getId());
        notifyCustomer();
    }

    public void completeOrder(CustomOrderBean customOrderBean, ProgressNoteBean progressNoteBean){
        ProgressNote progressNote = new ProgressNote(progressNoteBean.getComment(), progressNoteBean.getDate());
        CustomOrder customOrder = customOrderDao.selectCustomOrderById(customOrderBean.getId());
        customOrder.addProgressNoteOrder(progressNote);
        progressNoteDao.saveProgressNote(progressNote, customOrder.getId());
        customOrder.setOrderStatus(OrderStatus.COMPLETED);
        customOrderDao.updateCustomOrder(customOrder);
        notifyCustomer();
    }

    public CustomOrderSummaryBean elaborateOrder(DeliveryDestinationBean deliveryDestinationBean, DeliveryPreferencesBean deliveryPreferencesBean, BoardBean boardBean){
       //provissori
        saveCreatedCustomizedBoard(boardBean);

        DeliveryDestination deliveryDestination = new DeliveryDestination(Region.fromString(deliveryDestinationBean.getRegion()),
                deliveryDestinationBean.getProvince(),
                deliveryDestinationBean.getCity(),
                deliveryDestinationBean.getStreetAddress());

        Board board = boardDao.selectBoardById(boardBean.getId());


        DeliveryPreferences deliveryPreferences = new DeliveryPreferences(deliveryPreferencesBean.getComment(), deliveryPreferencesBean.getPreferredTimeSlot());

        Customer customer = customerDao.selectCustomerByUsername(SessionManager.getInstance().getSession().getUsername());

        CustomOrder customOrder = new CustomOrder(customer,deliveryDestination,deliveryPreferences, board);
        customer.addSubmittedOrder(customOrder);

        int costCoins = customOrder.totalCost();
        Wallet wallet = customer.getWallet();
        paymentController.payWithCoins(wallet, costCoins);

        this.customOrderDao.saveCustomOrder(customOrder);
        notifyOrderCoordinator();

        CustomOrderSummaryBean customOrderSummaryBean = new CustomOrderSummaryBean();
        customOrderSummaryBean.setId(customOrder.getId());
        customOrderSummaryBean.setCreationDate(customOrder.creationDate());
        customOrderSummaryBean.setDescriptionBoard(customOrder.getBoard().description());
        customOrderSummaryBean.setEstimatedDays(customOrder.getDeliveryDestination().estimatedDeliveryDays());
        customOrderSummaryBean.setStatus(customOrder.getOrderStatus().toString());
        customOrderSummaryBean.setNameBoard(customOrder.getBoard().name());
        customOrderSummaryBean.setTotalCost(customOrder.totalCost());
        return customOrderSummaryBean;
    }


    public List<CustomOrderBean> getAllOrders(){
       List<CustomOrder> customOrderList = customOrderDao.selectAllOpenOrder();
       List<CustomOrderBean> customOrderBeanList = new ArrayList<>();
       for(CustomOrder customOrder:customOrderList){
           CustomOrderBean customOrderBean = new CustomOrderBean();
           customOrderBean.setId(customOrder.getId());
           customOrderBean.setCreationDate(customOrder.creationDate());
           customOrderBean.setDescriptionBoard(customOrder.getBoard().description());
           customOrderBean.setStatus(customOrder.getOrderStatus().toString());
           customOrderBeanList.add(customOrderBean);
       }
        return customOrderBeanList;
    }


    public List<CustomOrderBean> getOrdersSubmitted(){
        Customer customer = customerDao.selectCustomerByUsername(SessionManager.getInstance().getSession().getUsername());
        List<CustomOrderBean> customOrderBeanList = new ArrayList<>();
        for(CustomOrder customOrder: customer.customOrdersSubmitted()){
            CustomOrderBean customOrderBean = new CustomOrderBean();
            customOrderBean.setId(customOrder.getId());
            customOrderBean.setCreationDate(customOrder.creationDate());
            customOrderBean.setStatus(customOrder.getOrderStatus().toString());
            customOrderBean.setDescriptionBoard(customOrder.getBoard().description());
            customOrderBeanList.add(customOrderBean);
        }
        return customOrderBeanList;
    }


    public List<CustomOrderBean> getOrdersAcquired(){
        Customer customer = customerDao.selectCustomerByUsername(SessionManager.getInstance().getSession().getUsername());
        List<CustomOrderBean> customOrderBeanList = new ArrayList<>();
        for(CustomOrder customOrder: customer.customOrdersAcquired()){
            CustomOrderBean customOrderBean = new CustomOrderBean();
            customOrderBean.setId(customOrder.getId());
            customOrderBean.setCreationDate(customOrder.creationDate());
            customOrderBean.setStatus(customOrder.getOrderStatus().toString());
            customOrderBean.setDescriptionBoard(customOrder.getBoard().description());
            customOrderBeanList.add(customOrderBean);
        }
        return customOrderBeanList;
    }

    public CustomOrderSummaryBean getMoreInfoOnOrder(CustomOrderBean customOrderBean){
        CustomOrder customOrder = customOrderDao.selectCustomOrderById(customOrderBean.getId());
        CustomOrderSummaryBean customOrderSummaryBean = new CustomOrderSummaryBean();
        customOrderSummaryBean.setId(customOrder.getId());
        customOrderSummaryBean.setCreationDate(customOrder.creationDate());
        customOrderSummaryBean.setDescriptionBoard(customOrder.getBoard().description());
        customOrderSummaryBean.setEstimatedDays(customOrder.getDeliveryDestination().estimatedDeliveryDays());
        customOrderSummaryBean.setStatus(customOrder.getOrderStatus().toString());
        customOrderSummaryBean.setNameBoard(customOrder.getBoard().name());
        customOrderSummaryBean.setTotalCost(customOrder.totalCost());
        return customOrderSummaryBean;
    }


    public List<ProgressNoteBean> progressNoteBeanList(CustomOrderSummaryBean customOrderBean){
        CustomOrder customOrder = customOrderDao.selectCustomOrderById(customOrderBean.getId());
        List<ProgressNote> progressNoteList = customOrder.progressDetails();
        List<ProgressNoteBean> progressNoteBeanList = new ArrayList<>();
        for(ProgressNote progressNote: progressNoteList){
            ProgressNoteBean progressNoteBean = new ProgressNoteBean();
            progressNoteBean.setComment(progressNote.getComment());
            progressNoteBean.setDate(progressNote.getDate());
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
}

