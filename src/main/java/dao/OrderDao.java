package dao;

import exceptions.DataAccessException;
import model.Order;

import java.util.List;

public interface OrderDao {
    Order selectOrderByCode(String orderCode) throws DataAccessException;
    List<Order> selectAllOpenOrder() throws DataAccessException;
    void saveOrder(Order order) throws DataAccessException;
    void updateOrder(Order order) throws DataAccessException;
}
