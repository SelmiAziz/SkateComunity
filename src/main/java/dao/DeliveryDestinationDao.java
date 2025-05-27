package dao;

import exceptions.DataAccessException;
import model.DeliveryDestination;


public interface DeliveryDestinationDao {
    void saveDeliveryDestination(DeliveryDestination deliveryDestination) throws DataAccessException;
    DeliveryDestination selectDeliveryDestinationById(String id) throws DataAccessException;
}
