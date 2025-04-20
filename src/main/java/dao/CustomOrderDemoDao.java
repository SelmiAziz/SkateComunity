package dao;

import model.CustomOrder;

public class CustomOrderDemoDao implements CustomOrderDao{
    private static CustomOrderDemoDao instance;


    public static synchronized CustomOrderDemoDao getInstance(){
        if(instance == null){
            instance = new CustomOrderDemoDao();
        }
        return instance;
    }

    @Override
    public void updateCustomOrder(CustomOrder customOrder) {

    }

    @Override
    public CustomOrder selectCustomOrderById(String id) {
        return null;
    }

    @Override
    public void saveCustomOrder(CustomOrder customOrder) {

    }
}
