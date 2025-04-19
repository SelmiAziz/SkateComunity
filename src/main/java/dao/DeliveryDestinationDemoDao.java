package dao;

import model.DeliveryDestination;

public class DeliveryDestinationDemoDao {
    private static DeliveryDestinationDemoDao instance;


    public static synchronized DeliveryDestinationDemoDao getInstance(){
        if(instance == null){
            instance = new DeliveryDestinationDemoDao();
        }
        return instance;
    }
}
