package dao;

import dao.patternAbstractFactory.DaoFactory;
import model.CustomOrder;

import java.util.ArrayList;
import java.util.List;

public class CustomOrderDemoDao implements CustomOrderDao{
    private static CustomOrderDemoDao instance;
    private final List<CustomOrder> customOrderList = new ArrayList<>();
    private final DeliveryDestinationDao deliveryDestinationDao = DaoFactory.getInstance().createDeliveryDestinationDao();


    public static synchronized CustomOrderDemoDao getInstance(){
        if(instance == null){
            instance = new CustomOrderDemoDao();
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
    public void updateCustomOrder(CustomOrder customOrder) {
        //it is not needed in demo
    }
}
