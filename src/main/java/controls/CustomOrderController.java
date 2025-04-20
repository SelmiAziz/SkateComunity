package controls;

import beans.*;
import dao.*;
import dao.patternAbstractFactory.DaoFactory;

import model.*;
import model.decorator.*;
import utils.SessionManager;
import view.CoordinatorOrderPageView;
import view.CustomerMakeOrdersPageView;

import java.util.ArrayList;
import java.util.List;

public class CustomOrderController {
    private final BoardDao boardDao = DaoFactory.getInstance().createBoardDao();
    private final CustomOrderDao customOrderDao = DaoFactory.getInstance().createCustomOrderDao();
    private final CustomerDao customerDao = DaoFactory.getInstance().createCostumerDao();
    private final ProgressNoteDao progressNoteDao = DaoFactory.getInstance().createProgressNoteDao();
    private CustomerMakeOrdersPageView makeOrdersPageView;
    private CoordinatorOrderPageView  coordinatorOrderPageView;

    private final SessionManager sessionManager = SessionManager.getInstance();

    public List<BoardBean> getBoardSamples(){
        List<BoardBean> boardBeanList = new ArrayList<>();
        List<Board> boardList = boardDao.selectAvailableBoards();
        for(Board board : boardList){
            BoardBean boardBean = new BoardBean(board.name(), board.description(), board.size(), board.price());
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
        boardBean.setName(baseBoard.name());
        boardBean.setDescription(piles.description());
        boardBean.setSize(baseBoard.size());
        boardBean.setPrice(piles.price());

        return boardBean;
    }

    public void saveCreatedDesignBoard(BoardBean boardBean){
        Customer customer = customerDao.selectCustomerByUsername(SessionManager.getInstance().getSession().getUsername());
        Board board = new BoardBase(boardBean.getName(), boardBean.getDescription(), boardBean.getSize(), boardBean.getPrice());
        customer.addDesignBoard(board);
        boardDao.addBoard(board, customer.getUsername());
    }

    protected void notifyOrderCoordinator() {
        coordinatorOrderPageView.newOrder();
    }

    protected void notifyCustomer(){
        makeOrdersPageView.orderUpdate();
    }

    public void acceptCustomOrder(CustomOrderBean customOrderBean, boolean accept){
        CustomOrder customOrder = customOrderDao.selectCustomOrderById(customOrderBean.getId());
        if(accept){
            customOrder.setOrderStatus(OrderStatus.PROCESSING);
        }else{
            customOrder.setOrderStatus(OrderStatus.REJECTED);
        }
        customOrderDao.updateCustomOrder(customOrder);
        notifyCustomer();
    }

    public void writeNote(ProgressNoteBean progressNoteBean, CustomOrderBean customOrderBean){
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

    public CustomOrderBean elaborateOrder(DeliveryDestinationBean deliveryDestinationBean, DeliveryPreferencesBean deliveryPreferencesBean, BoardBean boardBean){
        DeliveryDestination deliveryDestination = new DeliveryDestination(Region.fromString(deliveryDestinationBean.getRegion()),
                deliveryDestinationBean.getProvince(),
                deliveryDestinationBean.getCity(),
                deliveryDestinationBean.getStreetAddress());

        Board board = boardDao.selectBoardById(boardBean.getId());


        DeliveryPreferences deliveryPreferences = new DeliveryPreferences(deliveryPreferencesBean.getComment(), deliveryPreferencesBean.getPreferredTimeSlot());


        Customer customer = customerDao.selectCustomerByUsername(SessionManager.getInstance().getSession().getUsername());

        CustomOrder customOrder = new CustomOrder(customer,deliveryDestination,deliveryPreferences, board);
        customer.addSubmittedOrder(customOrder);

        notifyOrderCoordinator();

        CustomOrderBean customOrderBean = new  CustomOrderBean();
        customOrderBean.setId(customOrder.getId());
        customOrderBean.setCreationDate(customOrder.creationDate());
        customOrderBean.setEstimatedDays(customOrder.getDeliveryDestination().estimatedDeliveryDays());
        customOrderBean.setStatus(customOrder.getOrderStatus().toString());
        customOrderBean.setNameBoard(customOrder.getBoard().name());
        customOrderBean.setTotalCost(customOrder.totalCost());
        return customOrderBean;
    }


    public List<CustomOrderBean> getOrdersSubmitted(){
        Customer customer = customerDao.selectCustomerByUsername(SessionManager.getInstance().getSession().getUsername());
        List<CustomOrderBean> customOrderBeanList = new ArrayList<>();
        for(CustomOrder customOrder: customer.getOrdersSubmittedList()){
            CustomOrderBean customOrderBean = new CustomOrderBean();
            customOrderBean.setId(customOrder.getId());
            customOrderBean.setCreationDate(customOrder.creationDate());
            customOrderBean.setEstimatedDays(customOrder.getDeliveryDestination().estimatedDeliveryDays());
            customOrderBean.setStatus(customOrder.getOrderStatus().toString());
            customOrderBean.setNameBoard(customOrder.getBoard().name());
            customOrderBean.setTotalCost(customOrder.totalCost());
            customOrderBean.setRegionDestination(customOrder.getDeliveryDestination().getRegion().toString());
            customOrderBean.setProvinceDestination(customOrder.getDeliveryDestination().getProvince());
            customOrderBean.setCityDestination(customOrder.getDeliveryDestination().getCity());
            customOrderBean.setStreetAddersDestination(customOrder.getDeliveryDestination().getStreetAddress());
            customOrderBean.setStatus(customOrder.getOrderStatus().toString());
            customOrderBeanList.add(customOrderBean);
        }
        return customOrderBeanList;
    }


    public List<CustomOrderBean> getOrdersAcquired(){
        Customer customer = customerDao.selectCustomerByUsername(SessionManager.getInstance().getSession().getUsername());
        List<CustomOrderBean> customOrderBeanList = new ArrayList<>();
        for(CustomOrder customOrder: customer.getOrdersAcquiredList()){
            CustomOrderBean customOrderBean = new CustomOrderBean();
            customOrderBean.setId(customOrder.getId());
            customOrderBean.setCreationDate(customOrder.creationDate());
            customOrderBean.setEstimatedDays(customOrder.getDeliveryDestination().estimatedDeliveryDays());
            customOrderBean.setStatus(customOrder.getOrderStatus().toString());
            customOrderBean.setNameBoard(customOrder.getBoard().name());
            customOrderBean.setTotalCost(customOrder.totalCost());
            customOrderBean.setRegionDestination(customOrder.getDeliveryDestination().getRegion().toString());
            customOrderBean.setProvinceDestination(customOrder.getDeliveryDestination().getProvince());
            customOrderBean.setCityDestination(customOrder.getDeliveryDestination().getCity());
            customOrderBean.setStreetAddersDestination(customOrder.getDeliveryDestination().getStreetAddress());
            customOrderBean.setDeliveryDate(customOrder.deliveryDate());
            customOrderBeanList.add(customOrderBean);
        }
        return customOrderBeanList;
    }


    public List<ProgressNoteBean> progressNoteBeanList(CustomOrderBean customOrderBean){
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

