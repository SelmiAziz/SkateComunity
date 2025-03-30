package dao;

import dao.patternAbstractFactory.DaoFactory;
import model.Customer;

import java.util.ArrayList;
import java.util.List;

public class CustomerFileSystemDao implements CustomerDao{
    private static CustomerFileSystemDao instance;
    private final UserDao userDao = DaoFactory.getInstance().createUserDao();
    private final List<Customer> customerList;

    CustomerFileSystemDao(){
        this.customerList = new ArrayList<>();
    }

    public static synchronized CustomerFileSystemDao getInstance(){
        if(instance == null){
            instance = new CustomerFileSystemDao();
        }
        return instance;
    }

    @Override
    public Customer selectCustomerByUsername(String username) {
        return null;
    }

    @Override
    public void addCustomer(Customer customer) {
        this.customerList.add(customer);

        userDao.addUser(customer);


    }
}
