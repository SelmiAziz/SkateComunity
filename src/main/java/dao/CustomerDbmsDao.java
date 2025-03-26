package dao;

import dao.patternAbstractFactory.DaoFactory;
import model.Customer;
import utils.DbsConnector;
import utils.SkaterLevel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerDbmsDao implements CustomerDao{
    private static CustomerDbmsDao instance;
    private final List<Customer> costumerList = new ArrayList<>();

    public static synchronized CustomerDbmsDao getInstance(){
        if(instance == null){
            instance = new CustomerDbmsDao();
        }
        return instance;
    }

    //this is not good you have to recover all the registrations
    @Override
    public Customer selectCustomerByUsername(String  profileName) {
        for(Customer costumer:costumerList){
            if(costumer.getUsername().equals(profileName)){
                return costumer;
            }
        }
        String query = "SELECT u.username, u.password, u.dateOfBirth, c.skaterLevel, " +
                "GROUP_CONCAT(r.idRegistration) AS registrationIds " +
                "FROM users u " +
                "LEFT JOIN registrations r ON r.customerUsername = u.username " +
                "LEFT JOIN customers c ON u.username = c.customerUsername " +
                "WHERE u.username = ? " +
                "GROUP BY u.username, u.password, u.dateOfBirth, c.skaterLevel";

        Connection connection = DbsConnector.getInstance().getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, profileName);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String costumerUsername = resultSet.getString("username");
                    String password = resultSet.getString("password");
                    String skaterLevel = resultSet.getString("skaterLevel");
                    SkaterLevel skaterLevelEn = skaterLevel.equals("Novice") ? SkaterLevel.NOVICE :
                                                skaterLevel.equals("Advanced") ? SkaterLevel.ADVANCED : SkaterLevel.PROFICIENT;
                    String dateOfBirth = resultSet.getString("dateOfBirth");
                    String registrationIdsStr = resultSet.getString("registrationIds");
                    Customer costumer = new Customer(costumerUsername, password, dateOfBirth,skaterLevelEn );
                    if(registrationIdsStr != null){
                        String[] arrRegistrationIds = registrationIdsStr.split(",");
                    }
                    costumerList.add(costumer);
                    return costumer;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public void addCustomer(Customer customer) {
        //first adding the costumer in memory
        costumerList.add(customer);

        UserDao userDao = DaoFactory.getInstance().createUserDao();
        userDao.addUser(customer);
        System.out.println(("Ya fra"));

        String query = "INSERT INTO customers (customerUsername, skaterLevel) VALUES (?, ?)";
        Connection connection = DbsConnector.getInstance().getConnection();
        String username = customer.getUsername();
        String skaterLevel = customer.getSkaterLevel() == SkaterLevel.NOVICE ? "Novice":
                             customer.getSkaterLevel() == SkaterLevel.ADVANCED ? "Advanced": "Proficient";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, skaterLevel);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void update() {
        String query = "UPDATE profiles SET numCoins = ? WHERE profileName = ?";
        Connection connection = DbsConnector.getInstance().getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
