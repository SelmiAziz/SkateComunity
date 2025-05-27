package CustomerDaoTest;

import dao.CustomerDao;
import dao.patternabstractfactory.DaoFactory;
import exceptions.DataAccessException;
import model.Customer;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CustomerDaoTest {

    @Test
    void testAddCustomer() throws DataAccessException {
        CustomerDao customerDao = DaoFactory.getInstance().createCostumerDao();
        Customer customer = new Customer("testUsername");

        customerDao.saveCustomer(customer);
        Customer customerRetrieved = customerDao.selectCustomerByUsername(customer.getUsername());

        // Check that a Customer instance was added and matches the original
        boolean success = customerRetrieved != null
                && customerRetrieved.getUsername().equals("testUsername");

        assertTrue(success);
    }
}

