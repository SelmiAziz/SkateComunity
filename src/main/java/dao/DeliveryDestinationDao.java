package dao;

import model.DeliveryDestination;

public interface DeliveryDestinationDao {
    void saveDeliveryDestination(DeliveryDestination deliveryDestination);
    DeliveryDestination selectDeliveryDestinationById(String id);
}
