package dao;

public class DeliveryDestinationDbmsDao {

    private static DeliveryDestinationDbmsDao instance;


    public static synchronized DeliveryDestinationDbmsDao getInstance(){
        if(instance == null){
            instance = new DeliveryDestinationDbmsDao();
        }
        return instance;
    }
}
