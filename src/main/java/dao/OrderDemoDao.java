package dao;

import dao.patternAbstractFactory.DaoFactory;
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
    public Order selectCustomOrderById(String id) {
        for(Order customOrder: this.customOrderList){
            if(customOrder.getId().equals(id)){
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
    public void saveCustomOrder(Order customOrder) {
        customOrderList.add(customOrder);
        deliveryDestinationDao.saveDeliveryDestination(customOrder.getDeliveryDestination());
    }

    @Override
    public void updateCustomOrder(Order customOrder) {
        //it is not needed in demo
    }
}
