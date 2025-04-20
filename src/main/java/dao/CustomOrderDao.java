package dao;

import model.CustomOrder;

import java.util.List;

public interface CustomOrderDao {
    public CustomOrder selectCustomOrderById(String id);
    public List<CustomOrder> selectAllOpenOrder();
    public void saveCustomOrder(CustomOrder customOrder); 
    public void updateCustomOrder(CustomOrder customOrder);
}
