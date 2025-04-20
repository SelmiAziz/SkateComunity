package dao;

import model.CustomOrder;

public interface CustomOrderDao {
    public CustomOrder selectCustomOrderById(String id);
    public void saveCustomOrder(CustomOrder customOrder); 
    public void updateCustomOrder(CustomOrder customOrder);
}
