package controls;

import beans.*;
import dao.*;
import dao.patternAbstractFactory.DaoFactory;

import model.*;
import model.decorator.*;
import utils.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class CustomOrderController {
    private final BoardDao boardDao = DaoFactory.getInstance().createBoardDao();
    private final CustomOrderDao customOrderDao = DaoFactory.getInstance().createCustomOrderDao();
    private final DeliveryDestinationDao deliveryDestinationDao = DaoFactory.getInstance().createDeliveryDestinationDao();
    private final CustomerDao customerDao = DaoFactory.getInstance().createCostumerDao();
    private final ProgressNoteDao progressNoteDao = DaoFactory.getInstance().createProgressNoteDao();

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

    protected void notifyOrderCoordinator() {

    }

    public CustomOrderBean elaborateOrder(DeliveryDestinationBean deliveryDestinationBean, DeliveryPreferencesBean deliveryPreferencesBean, BoardBean boardBean){
        DeliveryDestination deliveryDestination = new DeliveryDestination(Region.fromString(deliveryDestinationBean.getRegion()),
                deliveryDestinationBean.getProvince(),
                deliveryDestinationBean.getCity(),
                deliveryDestinationBean.getStreetAddress());

        Board board = new BoardBase(boardBean.getName(), boardBean.getDescription(), boardBean.getSize(), boardBean.getPrice());


        DeliveryPreferences deliveryPreferences = new DeliveryPreferences(deliveryPreferencesBean.getComment(), deliveryPreferencesBean.getPreferredTimeSlot());


        Customer customer = customerDao.selectCustomerByUsername(SessionManager.getInstance().getSession().getUsername());

        CustomOrder customOrder = new CustomOrder(customer,deliveryDestination,deliveryPreferences, board);
        customer.addSubmittedOrder(customOrder);

        notifyOrderCoordinator();

        CustomOrderBean customOrderBean = new  CustomOrderBean();
        customOrderBean.setId(customOrder.getId());
        customOrderBean.setDate(customOrder.creationDate());
        customOrderBean.setEstimatedDays(customOrder.getDeliveryDestination().estimatedDeliveryDays());
        customOrderBean.setStatus(customOrder.getOrderStatus().toString());
        customOrderBean.setNameBoard(customOrder.getBoard().name());
        customOrderBean.setTotalCost(customOrder.totalCost());
        return customOrderBean;
    }
}

