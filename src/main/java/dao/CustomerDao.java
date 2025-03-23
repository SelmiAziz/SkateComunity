package dao;

import model.Customer;

public interface CustomerDao {
    void addCustomer(Customer user);
    Customer selectCustomerByUsername(String username);
}
