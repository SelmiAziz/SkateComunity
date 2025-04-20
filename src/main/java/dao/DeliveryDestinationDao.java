package dao;

import model.DeliveryDestination;

public interface DeliveryDestinationDao {
    void saveDeliveryDestination(DeliveryDestination deliveryDestination);
    DeliveryDestination selectDestinationById(String id);
}
