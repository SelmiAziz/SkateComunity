package dao;

import dao.patternAbstractFactory.DaoFactory;
import model.Customer;
import model.Order;
import model.OrderStatus;
import model.Wallet;
import model.decorator.Board;
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
    private final List<Customer> customerList = new ArrayList<>();
    private final WalletDao walletDao = DaoFactory.getInstance().createWalletDao();
    private final BoardDao boardDao = DaoFactory.getInstance().createBoardDao();
    private final OrderDao orderDao = DaoFactory.getInstance().createCustomOrderDao();

    public static synchronized CustomerDbmsDao getInstance(){
        if(instance == null){
            instance = new CustomerDbmsDao();
        }
        return instance;
    }

    @Override
    public Customer selectCustomerByUsername(String username) {
        for (Customer customer : this.customerList) {
            if (customer.getUsername().equals(username)) {
                return customer;
            }
        }

        String query = "SELECT u.username, u.password, u.dateOfBirth, w.walletId, c.skaterLevel, " +
                "GROUP_CONCAT(DISTINCT b.id) AS boardsIds, " +
                "GROUP_CONCAT(DISTINCT o.id) AS ordersIds " +
                "FROM users u " +
                "LEFT JOIN customers c ON u.username = c.customerUsername " +
                "LEFT JOIN wallets w ON w.walletOwner = c.customerUsername " +
                "LEFT JOIN boardDesigned b ON b.customerUsername = c.customerUsername " +
                "LEFT JOIN orders o ON o.customerUsername = c.customerUsername " +
                "WHERE u.username = ? " +
                "GROUP BY u.username, u.password, u.dateOfBirth, w.walletId, c.skaterLevel";

        Connection connection = DbsConnector.getInstance().getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String customerUsername = resultSet.getString("username");
                    String password = resultSet.getString("password");
                    String skaterLevel = resultSet.getString("skaterLevel");
                    SkaterLevel skaterLevelEn = skaterLevel.equals("Novice") ? SkaterLevel.NOVICE :
                            skaterLevel.equals("Advanced") ? SkaterLevel.ADVANCED : SkaterLevel.PROFICIENT;
                    String dateOfBirth = resultSet.getString("dateOfBirth");
                    int walletId = resultSet.getInt("walletId");

                    Wallet wallet = walletDao.selectWalletById(walletId);
                    Customer customer = new Customer(customerUsername, password, dateOfBirth, skaterLevelEn, wallet);

                    String boardIdsStr = resultSet.getString("boardsIds");
                    String orderIdsStr = resultSet.getString("ordersIds");

                    if (boardIdsStr != null && !boardIdsStr.isEmpty()) {
                        for (String boardId : boardIdsStr.split(",")) {
                            boardId = boardId.trim();
                            Board board = boardDao.selectBoardById(boardId);
                            if(board != null){
                                customer.addDesignBoard(board);
                            }
                        }
                    }

                    if (orderIdsStr != null && !orderIdsStr.isEmpty()) {
                        for (String orderId : orderIdsStr.split(",")) {
                            orderId = orderId.trim();
                            Order order = orderDao.selectCustomOrderById(orderId);
                            order.setCustomer(customer);
                            if (order != null) {
                                if (order.getOrderStatus() == OrderStatus.COMPLETED) {
                                    customer.addAcquiredOrder(order);
                                }
                                customer.addSubmittedOrder(order);
                                order.setCustomer(customer);
                            }
                        }
                    }

                    this.customerList.add(customer);
                    return customer;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }




    @Override
    public void addCustomer(Customer customer) {
        customerList.add(customer);

        UserDao userDao = DaoFactory.getInstance().createUserDao();
        userDao.addUser(customer);

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

        walletDao.addWallet(customer.getWallet(), customer.getUsername());
    }



}
