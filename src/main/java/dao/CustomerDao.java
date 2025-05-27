package dao;

import exceptions.DataAccessException;
import model.Customer;

import java.io.IOException;

public interface CustomerDao {
    void saveCustomer(Customer user) throws DataAccessException;
    Customer selectCustomerByUsername(String username) throws DataAccessException;
}
