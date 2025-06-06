package dao;

import dao.patternabstractfactory.DaoFactory;
import exceptions.DataAccessException;
import model.Order;
import model.OrderStatus;

import java.util.ArrayList;
import java.util.List;

public class OrderDemoDao implements OrderDao {
    private static OrderDemoDao instance;
    private final List<Order> customOrderList = new ArrayList<>();
    private final DeliveryDestinationDao deliveryDestinationDao = DaoFactory.getInstance().createDeliveryDestinationDao();


    public static synchronized OrderDemoDao getInstance(){
        if(instance == null){
            instance = new OrderDemoDao();
        }
        return instance;
    }


    @Override
    public Order selectOrderByCode(String code) {
        for(Order customOrder: this.customOrderList){
            if(customOrder.getOrderCode().equals(code)){
                return customOrder;
            }
        }
        return null;
    }

    @Override
    public List<Order> selectAllOpenOrder() {
        List<Order> openCustomOrderList = new ArrayList<>();
        for(Order customOrder:this.customOrderList){
            if(customOrder.getOrderStatus() == OrderStatus.REQUESTED || customOrder.getOrderStatus() == OrderStatus.PROCESSING){
                openCustomOrderList.add(customOrder);
            }
        }
        return openCustomOrderList;
    }

    @Override
    public void saveOrder(Order customOrder) throws DataAccessException {
        customOrderList.add(customOrder);
        deliveryDestinationDao.saveDeliveryDestination(customOrder.getDeliveryDestination());
    }

    @Override
    public void updateOrder(Order customOrder) {
        //it is not needed in demo
    }
}
