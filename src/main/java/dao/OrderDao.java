package dao;

import model.Order;

import java.util.List;

public interface OrderDao {
    public Order selectCustomOrderById(String id);
    public List<Order> selectAllOpenOrder();
    public void saveCustomOrder(Order customOrder);
    public void updateCustomOrder(Order customOrder);
}
