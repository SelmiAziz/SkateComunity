package dao;

import dao.patternAbstractFactory.DaoFactory;
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
    private final String fdCustomer = "src/main/resources/csv/customers.csv";
    private final String fdUser = "src/main/resources/csv/users.csv";
    private final String fdWallet = "src/main/resources/csv/wallets.csv";

    CustomerFileSystemDao() {
        this.customerList = new ArrayList<>();
    }

    public static synchronized CustomerFileSystemDao getInstance() {
        if (instance == null) {
            instance = new CustomerFileSystemDao();
        }
        return instance;
    }

    private int findWalletId(String username) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fdWallet))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] arr = line.split(",");
                if (username.equals(arr[2])) {
                    return Integer.parseInt(arr[0]);
                }
            }
            throw new RuntimeException();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public Customer selectCustomerByUsername(String username) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fdUser))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] arr = line.split(",");
                if (arr[0].equals(username)) {
                    String password = arr[1];
                    String dateOfBirth = arr[2];
                    try (BufferedReader customerReader = new BufferedReader(new FileReader(fdCustomer))) {
                        while ((line = customerReader.readLine()) != null) {
                            String[] arro = line.split(",");
                            System.out.println(arr[0]+arr[1]);
                            if (arro[0].equals(username)) {
                                SkaterLevel skaterLevel = SkaterLevel.valueOf(arro[1].toUpperCase());
                                WalletDao walletDao = DaoFactory.getInstance().createWalletDao();
                                Wallet wallet = walletDao.selectWalletById(findWalletId(username));
                                return new Customer(username, password, dateOfBirth, skaterLevel, wallet);
                            }
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    return null;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public void addCustomer(Customer customer) throws IOException {
        this.customerList.add(customer);
        userDao.addUser(customer);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fdCustomer, true))) {
            writer.write(customer.getUsername()+","+customer.getSkaterLevel());
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        DaoFactory.getInstance().createWalletDao().addWallet(customer.getWallet(), customer.getUsername());
    }
}
