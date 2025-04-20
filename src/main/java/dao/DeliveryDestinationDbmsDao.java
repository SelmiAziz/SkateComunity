package dao;

import model.DeliveryDestination;

public class DeliveryDestinationDbmsDao implements DeliveryDestinationDao {

    private static DeliveryDestinationDbmsDao instance;


    public static synchronized DeliveryDestinationDbmsDao getInstance(){
        if(instance == null){
            instance = new DeliveryDestinationDbmsDao();
        }
        return instance;
    }

    @Override
    public DeliveryDestination selectDestinationById(String id) {
        return null;
    }

    @Override
    public void saveDeliveryDestination(DeliveryDestination deliveryDestination) {

    }
}
