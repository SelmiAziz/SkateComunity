package dao;

import model.Customer;

import java.util.ArrayList;
import java.util.List;

public class CustomerDemoDao implements CustomerDao {
    private static CustomerDemoDao instance;
    private final List<Customer>  customerList;

    public CustomerDemoDao() {
        this.customerList = new ArrayList<>();

    }

    public static synchronized  CustomerDemoDao getInstance() {
        if (instance == null) {
            instance = new CustomerDemoDao();
        }
        return instance;
    }

    public void addCustomer(Customer customer){
        this.customerList.add(customer);
    }

    public Customer selectCustomerByUsername(String username){
        for(Customer costumer:customerList){
            if(costumer.getUsername().equals(username)){
                return costumer;
          }
        }
        return null;
    }



}

