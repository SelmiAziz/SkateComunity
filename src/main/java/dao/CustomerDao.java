package dao;

import model.Customer;

import java.io.IOException;

public interface CustomerDao {
    void saveCustomer(Customer user) throws IOException;
    Customer selectCustomerByUsername(String username) throws IOException;
}
