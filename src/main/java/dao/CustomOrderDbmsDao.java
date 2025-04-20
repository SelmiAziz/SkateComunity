package dao;

import model.CustomOrder;

public class CustomOrderDbmsDao extends CustomOrderDao {
    private static CustomOrderDbmsDao instance;


    public static synchronized CustomOrderDbmsDao getInstance(){
        if(instance == null){
            instance = new CustomOrderDbmsDao();
        }
        return instance;
    }

    @Override
    public CustomOrder selectCustomOrderById(String id) {
        return null;
    }

    @Override
    public void saveCustomOrder(CustomOrder customOrder) {

    }

    @Override
    public void updateCustomOrder(CustomOrder customOrder) {

    }
}
