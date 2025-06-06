package controls;

import beans.*;
import dao.*;
import dao.patternabstractfactory.DaoFactory;

import exceptions.DataAccessException;
import exceptions.SessionExpiredException;
import model.*;
import model.decorator.*;
import utils.*;

import java.util.ArrayList;
import java.util.List;

public class CustomOrderController {
    private final BoardDao boardDao = DaoFactory.getInstance().createBoardDao();
    private final OrderDao customOrderDao = DaoFactory.getInstance().createCustomOrderDao();
    private final CustomerDao customerDao = DaoFactory.getInstance().createCustomerDao();
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

    public List<BoardProfileBean> getBoardSamples(String token) throws DataAccessException {
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

    public BoardProfileBean generateCustomBoard(String token, CustomBoardBean customBoardBean) throws DataAccessException{
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

    public BoardBean saveCreatedCustomizedBoard(String token, BoardProfileBean boardProfileBean) throws SessionExpiredException, DataAccessException {
        Session session = SessionManager.getInstance().getSessionByToken(token);
        if(session == null){
            throw new SessionExpiredException();
        }
            Customer customer = customerDao.selectCustomerByUsername(session.getUsername());
            Board board = new BoardBase(boardProfileBean.getName(), boardProfileBean.getDescription(), boardProfileBean.getSize(), boardProfileBean.getPrice());
            customer.addDesignBoard(board);
            boardDao.addBoard(board, customer.getUsername());
            BoardBean boardBean = new BoardBean();
            boardBean.setBoardCode(board.boardCode());
            return boardBean;
    }


    public List<BoardProfileBean> getCustomizedBoards(String token) throws DataAccessException{
        Session session = SessionManager.getInstance().getSessionByToken(token);
        if(session == null){
            throw new SessionExpiredException();
        }
            Customer customer = customerDao.selectCustomerByUsername(session.getUsername());
            List<BoardProfileBean> boardBeanList = new ArrayList<>();
            for (Board board : customer.customizedBoards()) {
                BoardProfileBean boardProfileBean = new BoardProfileBean();
                boardProfileBean.setId(board.boardCode());
                boardProfileBean.setDescription(board.description());
                boardProfileBean.setName(board.name());
                boardProfileBean.setSize(board.size());
                boardProfileBean.setPrice(board.price());
                boardBeanList.add(boardProfileBean);
            }
            return boardBeanList;
    }

    protected void notifyOrderCoordinator() {
        coordinatorOrderView.newOrder();
    }

    protected void notifyCustomer(){
        customerOrderView.orderUpdate();
    }

    public void acceptCustomOrder(OrderBean customOrderBean, boolean accept) throws  DataAccessException{
        Order customOrder = customOrderDao.selectOrderByCode(customOrderBean.getId());
        if(accept){
            customOrder.setOrderStatus(OrderStatus.PROCESSING);
        }else{
            customOrder.setOrderStatus(OrderStatus.REJECTED);
            int coinsRefunded = customOrder.totalCost();
            Wallet  wallet = customOrder.getCustomer().getWallet();
            wallet.depositCoins(coinsRefunded);
        }
        customOrderDao.updateOrder(customOrder);
        notifyCustomer();
    }


    public void writeNote(OrderBean customOrderBean, ProgressNoteBean progressNoteBean) throws DataAccessException{
        ProgressNote progressNote = new ProgressNote(progressNoteBean.getComment(),dateConverter.stringToLocalDate(progressNoteBean.getDate()));

        Order customOrder = customOrderDao.selectOrderByCode(customOrderBean.getId());
        customOrder.addProgressNote(progressNote);
        progressNoteDao.saveProgressNote(progressNote, customOrder.getOrderCode());
        notifyCustomer();
    }

    public void completeOrder(OrderBean customOrderBean, ProgressNoteBean progressNoteBean) throws  DataAccessException{
        ProgressNote progressNote = new ProgressNote(progressNoteBean.getComment(),dateConverter.stringToLocalDate( progressNoteBean.getDate()));
        Order customOrder = customOrderDao.selectOrderByCode(customOrderBean.getId());
        customOrder.addProgressNote(progressNote);
        progressNoteDao.saveProgressNote(progressNote, customOrder.getOrderCode());
        customOrder.setOrderStatus(OrderStatus.COMPLETED);
        customOrderDao.updateOrder(customOrder);
        notifyCustomer();
    }

    public OrderSummaryBean elaborateOrder(String token, DeliveryDestinationBean deliveryDestinationBean, DeliveryPreferencesBean deliveryPreferencesBean, BoardBean boardBean) throws SessionExpiredException, DataAccessException{
        Session session = SessionManager.getInstance().getSessionByToken(token);
        if(session == null){
            throw new SessionExpiredException();
        }
            DeliveryDestination deliveryDestination = new DeliveryDestination(Region.fromString(deliveryDestinationBean.getRegion()),
                    deliveryDestinationBean.getProvince(),
                    deliveryDestinationBean.getCity(),
                    deliveryDestinationBean.getStreetAddress());

            Board board = boardDao.selectBoardById(boardBean.getBoardCode());


            DeliveryPreferences deliveryPreferences = new DeliveryPreferences(deliveryPreferencesBean.getComment(), deliveryPreferencesBean.getPreferredTimeSlot());

            Customer customer = customerDao.selectCustomerByUsername(session.getUsername());

            Order customOrder = new Order(deliveryDestination, deliveryPreferences, board);
            customOrder.setCustomer(customer);
            customer.addSubmittedOrder(customOrder);

            int costCoins = customOrder.totalCost();
            Wallet wallet = customer.getWallet();
            paymentController.payWithCoins(wallet, costCoins);

            this.customOrderDao.saveOrder(customOrder);
            notifyOrderCoordinator();

            OrderSummaryBean customOrderSummaryBean = new OrderSummaryBean();
            customOrderSummaryBean.setOrderCode(customOrder.getOrderCode());
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


    public List<OrderSummaryBean> getAllOrders() throws DataAccessException{
       List<Order> orderList = customOrderDao.selectAllOpenOrder();
       List<OrderSummaryBean> customOrderBeanList = new ArrayList<>();
       for(Order order:orderList){
           OrderSummaryBean orderSummaryBean = new OrderSummaryBean();
           orderSummaryBean.setOrderCode(order.getOrderCode());
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


    public List<OrderSummaryBean> getSubmittedOrders(String token) throws DataAccessException  {
        Session session = SessionManager.getInstance().getSessionByToken(token);
        if(session == null){
            throw new SessionExpiredException();
        }
            Customer customer = customerDao.selectCustomerByUsername(session.getUsername());
            List<OrderSummaryBean> customOrderBeanList = new ArrayList<>();
            for (Order order : customer.customOrdersSubmitted()) {
                OrderSummaryBean orderSummaryBean = new OrderSummaryBean();
                orderSummaryBean.setOrderCode(order.getOrderCode());
                orderSummaryBean.setCreationDate(dateConverter.localDateToString(order.creationDate()));
                orderSummaryBean.setDeliveryDate(dateConverter.localDateToString(order.deliveryDate()));
                orderSummaryBean.setStatus(order.getOrderStatus().toString());
                orderSummaryBean.setTotalCost(order.totalCost());
                orderSummaryBean.setDescriptionBoard(order.getBoard().description());
                orderSummaryBean.setRegionDestination(order.getDeliveryDestination().getRegion().toString());
                orderSummaryBean.setProvinceDestination(order.getDeliveryDestination().getProvince());
                orderSummaryBean.setCityDestination(order.getDeliveryDestination().getCity());
                orderSummaryBean.setStreetAddersDestination(order.getDeliveryDestination().getStreetAddress());
                orderSummaryBean.setNameBoard(order.getBoard().name());
                orderSummaryBean.setEstimatedDays(order.getDeliveryDestination().estimatedDeliveryDays());
                customOrderBeanList.add(orderSummaryBean);
            }
            return customOrderBeanList;
    }


    public List<OrderSummaryBean> getCompletedOrders(String token) throws SessionExpiredException, DataAccessException{
        Session session = SessionManager.getInstance().getSessionByToken(token);
        if(session == null){
            throw new SessionExpiredException();
        }
            Customer customer = customerDao.selectCustomerByUsername(session.getUsername());
            List<OrderSummaryBean> customOrderBeanList = new ArrayList<>();
            for (Order order : customer.customOrdersAcquired()) {
                OrderSummaryBean orderSummaryBean = new OrderSummaryBean();
                orderSummaryBean.setOrderCode(order.getOrderCode());
                orderSummaryBean.setCreationDate(dateConverter.localDateToString(order.creationDate()));
                orderSummaryBean.setDeliveryDate(dateConverter.localDateToString(order.deliveryDate()));
                orderSummaryBean.setStatus(order.getOrderStatus().toString());
                orderSummaryBean.setRegionDestination(order.getDeliveryDestination().getRegion().toString());
                orderSummaryBean.setProvinceDestination(order.getDeliveryDestination().getProvince());
                orderSummaryBean.setCityDestination(order.getDeliveryDestination().getCity());
                orderSummaryBean.setStreetAddersDestination(order.getDeliveryDestination().getStreetAddress());
                orderSummaryBean.setDescriptionBoard(order.getBoard().description());
                orderSummaryBean.setNameBoard(order.getBoard().name());
                orderSummaryBean.setEstimatedDays(order.getDeliveryDestination().estimatedDeliveryDays());
                customOrderBeanList.add(orderSummaryBean);
            }
            return customOrderBeanList;

    }




    public List<ProgressNoteBean> getProgressNotesOrder(String token, OrderBean orderBean) throws SessionExpiredException, DataAccessException{
        Session session = SessionManager.getInstance().getSessionByToken(token);
        if(session == null){
            throw new SessionExpiredException();
        }
        Order order = customOrderDao.selectOrderByCode(orderBean.getId());
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

    public WalletBean walletDetails(String token) throws SessionExpiredException, DataAccessException{
        Session session = SessionManager.getInstance().getSessionByToken(token);
        if(session == null){
            throw new SessionExpiredException();
        }
        Customer customer = customerDao.selectCustomerByUsername(session.getUsername());
        Wallet wallet = customer.getWallet();
        WalletBean walletBean = new WalletBean();
        walletBean.setBalance(wallet.getBalance());
        return walletBean;
    }

}

