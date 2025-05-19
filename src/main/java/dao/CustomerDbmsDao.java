package dao;

import dao.patternAbstractFactory.DaoFactory;
import model.Customer;
import model.Order;
import model.OrderStatus;
import model.Wallet;
import model.decorator.Board;
import utils.DbsConnector;
import utils.SkaterLevel;

import java.io.IOException;
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
    public Customer selectCustomerByUsername(String username) throws IOException {
        for (Customer customer : this.customerList) {
            if (customer.getUsername().equals(username)) {
                return customer;
            }
        }

        try (Connection connection = DbsConnector.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(getCustomerQuery())) {

            stmt.setString(1, username);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Customer customer = buildCustomerFromResultSet(rs);
                    loadBoardsForCustomer(rs, customer);
                    loadOrdersForCustomer(rs, customer);
                    this.customerList.add(customer);
                    return customer;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
    private String getCustomerQuery() {
        return "SELECT u.username, u.password, u.dateOfBirth, w.walletId, c.skaterLevel, " +
                "GROUP_CONCAT(DISTINCT b.id) AS boardsIds, " +
                "GROUP_CONCAT(DISTINCT o.id) AS ordersIds " +
                "FROM users u " +
                "LEFT JOIN customers c ON u.username = c.customerUsername " +
                "LEFT JOIN wallets w ON w.walletOwner = c.customerUsername " +
                "LEFT JOIN boardDesigned b ON b.customerUsername = c.customerUsername " +
                "LEFT JOIN orders o ON o.customerUsername = c.customerUsername " +
                "WHERE u.username = ? " +
                "GROUP BY u.username, u.password, u.dateOfBirth, w.walletId, c.skaterLevel";
    }

    private Customer buildCustomerFromResultSet(ResultSet rs) throws SQLException, IOException {
        String username = rs.getString("username");
        String password = rs.getString("password");
        String dateOfBirth = rs.getString("dateOfBirth");
        String skaterLevelStr = rs.getString("skaterLevel");
        int walletId = rs.getInt("walletId");

        SkaterLevel skaterLevel = switch (skaterLevelStr) {
            case "Novice" -> SkaterLevel.NOVICE;
            case "Advanced" -> SkaterLevel.ADVANCED;
            default -> SkaterLevel.PROFICIENT;
        };

        Wallet wallet = walletDao.selectWalletById(walletId);
        return new Customer(username, password, dateOfBirth, skaterLevel, wallet);
    }

    private void loadBoardsForCustomer(ResultSet rs, Customer customer) throws SQLException {
        String boardIdsStr = rs.getString("boardsIds");
        if (boardIdsStr != null && !boardIdsStr.isEmpty()) {
            for (String boardId : boardIdsStr.split(",")) {
                Board board = boardDao.selectBoardById(boardId.trim());
                if (board != null) {
                    customer.addDesignBoard(board);
                }
            }
        }
    }

    private void loadOrdersForCustomer(ResultSet rs, Customer customer) throws SQLException {
        String orderIdsStr = rs.getString("ordersIds");
        if (orderIdsStr != null && !orderIdsStr.isEmpty()) {
            for (String orderId : orderIdsStr.split(",")) {
                Order order = orderDao.selectOrderByCode(orderId.trim());
                if (order != null) {
                    order.setCustomer(customer);
                    if (order.getOrderStatus() == OrderStatus.COMPLETED) {
                        customer.addAcquiredOrder(order);
                    }
                    customer.addSubmittedOrder(order);
                }
            }
        }
    }


    @Override
    public void saveCustomer(Customer customer) throws IOException {
        customerList.add(customer);

        UserDao userDao = DaoFactory.getInstance().createUserDao();
        userDao.addUser(customer);

        String query = "INSERT INTO customers (customerUsername, skaterLevel) VALUES (?, ?)";
        Connection connection = DbsConnector.getInstance().getConnection();
        String username = customer.getUsername();



        String skaterLevel;
        if (customer.getSkaterLevel() == SkaterLevel.NOVICE) {
            skaterLevel = "Novice";
        } else if (customer.getSkaterLevel() == SkaterLevel.ADVANCED) {
            skaterLevel = "Advanced";
        } else {
            skaterLevel = "Proficient";
        }

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, skaterLevel);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new IOException("Error inserting in database",e);
        }

        walletDao.saveWallet(customer.getWallet(), customer.getUsername());
    }



}
