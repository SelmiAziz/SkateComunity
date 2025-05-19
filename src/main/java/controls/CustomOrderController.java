package controls;

import beans.*;
import dao.*;
import dao.patternAbstractFactory.DaoFactory;

import exceptions.SessionExpiredException;
import model.*;
import model.decorator.*;
import utils.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CustomOrderController {
    private final BoardDao boardDao = DaoFactory.getInstance().createBoardDao();
    private final OrderDao customOrderDao = DaoFactory.getInstance().createCustomOrderDao();
    private final CustomerDao customerDao = DaoFactory.getInstance().createCostumerDao();
    private final ProgressNoteDao progressNoteDao = DaoFactory.getInstance().createProgressNoteDao();
    private CustomerOrderView customerOrderView;
    private CoordinatorOrderView coordinatorOrderView;
    private final DateConverter dateConverter = new DateConverter();
    private final PaymentController paymentController = new PaymentController();

    public void setCoordinatorOrderView(CoordinatorOrderView coordinatorOrderPageView) {
        this.coordinatorOrderView = coordinatorOrderPageView;
    }

    public void setCustomerOrderView(CustomerOrderView customerAllOrdersPageView) {
        this.customerOrderView = customerAllOrdersPageView;
    }

    public List<BoardProfileBean> getBoardSamples(String token){
        Session session = SessionManager.getInstance().getSessionByToken(token);
        if(session == null){
            throw new SessionExpiredException();
        }

        List<BoardProfileBean> boardBeanList = new ArrayList<>();
        List<Board> boardList = boardDao.selectAvailableBoards();
        for(Board board : boardList){
            BoardProfileBean boardProfileBean = new BoardProfileBean();
            boardProfileBean.setName(board.name());
            boardProfileBean.setDescription(board.description());
            boardProfileBean.setPrice(board.price());
            boardProfileBean.setSize(board.size());
            boardBeanList.add(boardProfileBean);
        }
        return boardBeanList;
    }

    public BoardProfileBean generateCustomBoard(String token, CustomBoardBean customBoardBean){
        Session session = SessionManager.getInstance().getSessionByToken(token);
        if(session == null){
            throw new SessionExpiredException();
        }

        Board board = boardDao.selectBoardByName(customBoardBean.getName());


        if(customBoardBean.getUseGripTexture()){
            board = new GripTextureDecorator(board, customBoardBean.getGripTexture());
        }

        if(customBoardBean.getUseConcaveNose()){
            board = new NoseConcaveDecorator(board, customBoardBean.getConcaveNose());
        }

        if(customBoardBean.getUseConcaveTail()){
            board = new TailConcaveDecorator(board, customBoardBean.getConcaveTail());
        }

        if(customBoardBean.getUseWarrantyMonths()){
            board = new WarrantyDecorator(board, customBoardBean.getWarrantyMonths());
        }

        if(customBoardBean.getUseExtraPiles()){
            board = new ExtraPilesDecorator(board, customBoardBean.getExtraPiles());
        }

        BoardProfileBean boardProfileBean = new BoardProfileBean();
        boardProfileBean.setName(board.name());
        boardProfileBean.setDescription(board.description());
        boardProfileBean.setSize(board.size());
        boardProfileBean.setPrice(board.price());

        return boardProfileBean;
    }

    public BoardBean saveCreatedCustomizedBoard(String token, BoardProfileBean boardProfileBean) throws SessionExpiredException {
        Session session = SessionManager.getInstance().getSessionByToken(token);
        if(session == null){
            throw new SessionExpiredException();
        }
        try {
            Customer customer = customerDao.selectCustomerByUsername(session.getUsername());
            Board board = new BoardBase(boardProfileBean.getName(), boardProfileBean.getDescription(), boardProfileBean.getSize(), boardProfileBean.getPrice());
            customer.addDesignBoard(board);
            boardDao.addBoard(board, customer.getUsername());
            BoardBean boardBean = new BoardBean();
            boardBean.setId(board.boardId());
            return boardBean;
        }catch(IOException _){
            throw new SessionExpiredException();
        }
    }


    public List<BoardProfileBean> getCustomizedBoards(String token){
        Session session = SessionManager.getInstance().getSessionByToken(token);
        if(session == null){
            throw new SessionExpiredException();
        }
        try {
            Customer customer = customerDao.selectCustomerByUsername(session.getUsername());
            List<BoardProfileBean> boardBeanList = new ArrayList<>();
            for (Board board : customer.customizedBoards()) {
                BoardProfileBean boardProfileBean = new BoardProfileBean();
                boardProfileBean.setId(board.boardId());
                boardProfileBean.setDescription(board.description());
                boardProfileBean.setName(board.name());
                boardProfileBean.setSize(board.size());
                boardProfileBean.setPrice(board.price());
                boardBeanList.add(boardProfileBean);
            }
            return boardBeanList;
        }catch(IOException _){
            throw new SessionExpiredException();
        }
    }

    protected void notifyOrderCoordinator() {
        coordinatorOrderView.newOrder();
    }

    protected void notifyCustomer(){
        customerOrderView.orderUpdate();
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
        customOrder.addProgressNote(progressNote);
        progressNoteDao.saveProgressNote(progressNote, customOrder.getId());
        notifyCustomer();
    }

    public void completeOrder(OrderBean customOrderBean, ProgressNoteBean progressNoteBean){
        ProgressNote progressNote = new ProgressNote(progressNoteBean.getComment(),dateConverter.stringToLocalDate( progressNoteBean.getDate()));
        Order customOrder = customOrderDao.selectCustomOrderById(customOrderBean.getId());
        customOrder.addProgressNote(progressNote);
        progressNoteDao.saveProgressNote(progressNote, customOrder.getId());
        customOrder.setOrderStatus(OrderStatus.COMPLETED);
        customOrderDao.updateCustomOrder(customOrder);
        notifyCustomer();
    }

    public OrderSummaryBean elaborateOrder(String token, DeliveryDestinationBean deliveryDestinationBean, DeliveryPreferencesBean deliveryPreferencesBean, BoardBean boardBean) throws SessionExpiredException {
        Session session = SessionManager.getInstance().getSessionByToken(token);
        if(session == null){
            throw new SessionExpiredException();
        }
        try {
            DeliveryDestination deliveryDestination = new DeliveryDestination(Region.fromString(deliveryDestinationBean.getRegion()),
                    deliveryDestinationBean.getProvince(),
                    deliveryDestinationBean.getCity(),
                    deliveryDestinationBean.getStreetAddress());

            Board board = boardDao.selectBoardById(boardBean.getId());


            DeliveryPreferences deliveryPreferences = new DeliveryPreferences(deliveryPreferencesBean.getComment(), deliveryPreferencesBean.getPreferredTimeSlot());

            Customer customer = customerDao.selectCustomerByUsername(session.getUsername());

            Order customOrder = new Order(deliveryDestination, deliveryPreferences, board);
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
        }catch(IOException _){
            throw new SessionExpiredException();
        }
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
           orderSummaryBean.setTotalCost(order.totalCost());
           orderSummaryBean.setEstimatedDays(order.getDeliveryDestination().estimatedDeliveryDays());
           customOrderBeanList.add(orderSummaryBean);
       }
        return customOrderBeanList;
    }


    public List<OrderSummaryBean> getSubmittedOrders(String token)  {
        Session session = SessionManager.getInstance().getSessionByToken(token);
        if(session == null){
            throw new SessionExpiredException();
        }
        try {
            Customer customer = customerDao.selectCustomerByUsername(session.getUsername());
            List<OrderSummaryBean> customOrderBeanList = new ArrayList<>();
            for (Order order : customer.customOrdersSubmitted()) {
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
        }catch(IOException _){
            throw new SessionExpiredException();
        }
    }


    public List<OrderSummaryBean> getCompletedOrders(String token) throws SessionExpiredException{
        Session session = SessionManager.getInstance().getSessionByToken(token);
        if(session == null){
            throw new SessionExpiredException();
        }
        try {
            Customer customer = customerDao.selectCustomerByUsername(session.getUsername());
            List<OrderSummaryBean> customOrderBeanList = new ArrayList<>();
            for (Order order : customer.customOrdersAcquired()) {
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
        }catch(IOException _){
            throw new SessionExpiredException();
        }
    }




    public List<ProgressNoteBean> getProgressNotesOrder(String token, OrderBean orderBean) throws SessionExpiredException{
        Session session = SessionManager.getInstance().getSessionByToken(token);
        if(session == null){
            throw new SessionExpiredException();
        }
        Order order = customOrderDao.selectCustomOrderById(orderBean.getId());
        List<ProgressNote> progressNoteList = order.progressNoteChronology();
        List<ProgressNoteBean> progressNoteBeanList = new ArrayList<>();
        for(ProgressNote progressNote: progressNoteList){
            ProgressNoteBean progressNoteBean = new ProgressNoteBean();
            progressNoteBean.setComment(progressNote.getComment());
            progressNoteBean.setDate(dateConverter.localDateToString(progressNote.getDate()));
            progressNoteBeanList.add(progressNoteBean);
        }
        return progressNoteBeanList;
    }

    public WalletBean walletDetails(String token) throws SessionExpiredException{
        Session session = SessionManager.getInstance().getSessionByToken(token);
        if(session == null){
            throw new SessionExpiredException();
        }
        try {
            Customer customer = customerDao.selectCustomerByUsername(session.getUsername());
            Wallet wallet = customer.getWallet();
            WalletBean walletBean = new WalletBean();
            walletBean.setBalance(wallet.getBalance());
            return walletBean;
        }catch(IOException _){
            throw new SessionExpiredException();
        }
    }

}

