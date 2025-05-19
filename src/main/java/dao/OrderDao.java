package dao;

import model.Order;

import java.util.List;

public interface OrderDao {
    Order selectOrderByCode(String orderCode);
    List<Order> selectAllOpenOrder();
    void saveOrder(Order order);
    void updateOrder(Order order);
}
