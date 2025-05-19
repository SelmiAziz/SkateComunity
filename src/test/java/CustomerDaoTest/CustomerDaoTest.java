package CustomerDaoTest;

import dao.CustomerDao;
import dao.patternAbstractFactory.DaoFactory;
import model.Customer;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertTrue;

class CustomerDaoTest {

    @Test
    void testAddCustomer() throws IOException {
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

