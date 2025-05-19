package dao;

import dao.patternabstractfactory.DaoFactory;
import model.Customer;
import model.Wallet;
import utils.SkaterLevel;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerFileSystemDao implements CustomerDao {
    private static CustomerFileSystemDao instance;
    private final UserDao userDao = DaoFactory.getInstance().createUserDao();
    private final List<Customer> customerList;
    private String fdCustomer = "src/main/resources/csv/customers.csv";
    private  String fdUser = "src/main/resources/csv/users.csv";
    private String fdWallet = "src/main/resources/csv/wallets.csv";

    CustomerFileSystemDao() {
        this.customerList = new ArrayList<>();
    }

    public static synchronized CustomerFileSystemDao getInstance() {
        if (instance == null) {
            instance = new CustomerFileSystemDao();
        }
        return instance;
    }

    private int findWalletId(String username) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(fdWallet))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] arr = line.split(",");
                if (username.equals(arr[2])) {
                    return Integer.parseInt(arr[0]);
                }
            }
        } catch (IOException e) {
            throw new IOException("Error reading the file", e);
        }
        return -1;
    }

    @Override
    public Customer selectCustomerByUsername(String username) throws IOException {
        String password = null;
        String dateOfBirth = null;

        try (BufferedReader reader = new BufferedReader(new FileReader(fdUser))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] arr = line.split(",");
                if (arr[0].equals(username)) {
                    password = arr[1];
                    dateOfBirth = arr[2];
                    break;
                }
            }
        } catch (IOException e) {
            throw new IOException("Error reading user file", e);
        }

        if (password == null || dateOfBirth == null) {
            return null;
        }

        return extractCustomerDetails(username, password, dateOfBirth);
    }

    private Customer extractCustomerDetails(String username, String password, String dateOfBirth) throws IOException {
        try (BufferedReader customerReader = new BufferedReader(new FileReader(fdCustomer))) {
            String line;
            while ((line = customerReader.readLine()) != null) {
                String[] arro = line.split(",");
                if (arro[0].equals(username)) {
                    SkaterLevel skaterLevel = SkaterLevel.valueOf(arro[1].toUpperCase());
                    WalletDao walletDao = DaoFactory.getInstance().createWalletDao();
                    Wallet wallet = walletDao.selectWalletById(findWalletId(username));
                    return new Customer(username, password, dateOfBirth, skaterLevel, wallet);
                }
            }
        } catch (IOException e) {
            throw new IOException("Error reading customer file", e);
        }
        return null;
    }


    @Override
    public void saveCustomer(Customer customer) throws IOException {
        this.customerList.add(customer);
        userDao.addUser(customer);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fdCustomer, true))) {
            writer.write(customer.getUsername()+","+customer.getSkaterLevel());
            writer.newLine();
        } catch (IOException e) {
            throw new IOException("Error writing on file",e);
        }
        DaoFactory.getInstance().createWalletDao().saveWallet(customer.getWallet(), customer.getUsername());
    }
}
