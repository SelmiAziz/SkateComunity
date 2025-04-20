package dao;

import dao.patternAbstractFactory.DaoFactory;
import model.CustomOrder;
import model.OrderStatus;

import java.util.ArrayList;
import java.util.List;

public class CustomOrderDbmsDao implements CustomOrderDao {
    private static CustomOrderDbmsDao instance;
    private final List<CustomOrder> customOrderList = new ArrayList<>();
    private final DeliveryDestinationDao deliveryDestinationDao = DaoFactory.getInstance().createDeliveryDestinationDao();

    public static synchronized CustomOrderDbmsDao getInstance(){
        if(instance == null){
            instance = new CustomOrderDbmsDao();
        }
        return instance;
    }

    @Override
    public CustomOrder selectCustomOrderById(String id) {
        for(CustomOrder customOrder: this.customOrderList){
            if(customOrder.getId().equals(id)){
                return customOrder;
            }
        }
        return null;
    }

    @Override
    public void saveCustomOrder(CustomOrder customOrder) {
        customOrderList.add(customOrder);
        deliveryDestinationDao.saveDeliveryDestination(customOrder.getDeliveryDestination());
    }

    @Override
    public List<CustomOrder> selectAllOpenOrder() {
        List<CustomOrder> openCustomOrderList = new ArrayList<>();
        for(CustomOrder customOrder:this.customOrderList){
            if(customOrder.getOrderStatus() == OrderStatus.REQUESTED || customOrder.getOrderStatus() == OrderStatus.PROCESSING){
                openCustomOrderList.add(customOrder);
            }
        }
        return openCustomOrderList;
    }



    @Override
    public void updateCustomOrder(CustomOrder customOrder) {

    }
}
