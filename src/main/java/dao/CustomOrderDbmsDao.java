package dao;

import dao.patternAbstractFactory.DaoFactory;
import model.*;
import model.decorator.Board;
import utils.DbsConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomOrderDbmsDao implements CustomOrderDao {
    private static CustomOrderDbmsDao instance;
    private final List<Order> customOrderList = new ArrayList<>();
    private final DeliveryDestinationDao deliveryDestinationDao = DaoFactory.getInstance().createDeliveryDestinationDao();
    private final BoardDao boardDao = DaoFactory.getInstance().createBoardDao();
    private final CustomerDao customerDao = DaoFactory.getInstance().createCostumerDao();

    public static synchronized CustomOrderDbmsDao getInstance(){
        if(instance == null){
            instance = new CustomOrderDbmsDao();
        }
        return instance;
    }

    @Override
    public Order selectCustomOrderById(String id) {
        for (Order order : this.customOrderList) {
            if (order.getId().equals(id)) {
                return order;
            }
        }

        String sql = "SELECT o.id, " +
                "o.customerUsername, " +
                "o.boardId, " +
                "o.comment, " +
                "o.preferredTimeSlot, " +
                "o.status " +
                "FROM orders o " +
                "WHERE o.id = ?";

        Connection conn = DbsConnector.getInstance().getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String customerUsername = rs.getString("customerUsername");
                String boardId = rs.getString("boardId");
                String comment = rs.getString("comment");
                String preferredTimeSlot = rs.getString("preferredTimeSlot");
                String orderStatusStr = rs.getString("status");

                Customer customer = customerDao.selectCustomerByUsername(customerUsername);

                Board board = boardDao.selectBoardById(boardId);


                DeliveryPreferences deliveryPreferences = new DeliveryPreferences(comment, preferredTimeSlot);
                DeliveryDestination deliveryDestination = new DeliveryDestination(Region.CALABRIA, "k", "m", "a");
                Order order = new Order(customer,deliveryDestination, deliveryPreferences , board);
                order.setId(id);
                order.setOrderStatus(OrderStatus.fromString(orderStatusStr));

                this.customOrderList.add(order);

                return order;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }




    @Override
    public void saveCustomOrder(Order order) {
        customOrderList.add(order);
        deliveryDestinationDao.saveDeliveryDestination(order.getDeliveryDestination());

        String sql = "INSERT INTO orders (id, customerUsername, comment, preferredTimeSlot, boardId, status) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DbsConnector.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, order.getId());
            stmt.setString(2, order.getCustomer().getUsername());
            stmt.setString(3, order.commentOrderPreferences());
            stmt.setString(4, order.timeSlotOrderPreferences());
            stmt.setString(5, order.getBoard().boardId());
            stmt.setString(6, order.getOrderStatus().toString());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public List<Order> selectAllOpenOrder() {
        List<Order> openCustomOrderList = new ArrayList<>();

        for(Order customOrder : this.customOrderList) {
            if(customOrder.getOrderStatus() == OrderStatus.REQUESTED || customOrder.getOrderStatus() == OrderStatus.PROCESSING) {
                openCustomOrderList.add(customOrder);
            }
        }

        if (!openCustomOrderList.isEmpty()) {
            //return openCustomOrderList;
        }

        String sql = "SELECT o.id, " +
                "o.customerUsername, " +
                "o.boardId, " +
                "o.comment, " +
                "o.preferredTimeSlot, " +
                "o.status " +
                "FROM orders o " +
                "WHERE o.status IN ('Requested', 'Processing')";

        Connection conn = DbsConnector.getInstance().getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String id = rs.getString("id");
                String customerUsername = rs.getString("customerUsername");
                String boardId = rs.getString("boardId");
                String comment = rs.getString("comment");
                String preferredTimeSlot = rs.getString("preferredTimeSlot");
                String orderStatusStr = rs.getString("status");

                Customer customer = customerDao.selectCustomerByUsername(customerUsername);
                Board board = boardDao.selectBoardById(boardId);

                DeliveryPreferences deliveryPreferences = new DeliveryPreferences(comment, preferredTimeSlot);
                DeliveryDestination deliveryDestination = new DeliveryDestination(Region.CALABRIA, "k", "m", "a");

                Order order = new Order(customer, deliveryDestination, deliveryPreferences, board);
                order.setId(id);
                order.setOrderStatus(OrderStatus.fromString(orderStatusStr));

                openCustomOrderList.add(order);
            }
            return openCustomOrderList;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return openCustomOrderList;
    }




    @Override
    public void updateCustomOrder(Order order) {
        String sql = "UPDATE orders SET status = ? WHERE id = ?";

        try (Connection conn = DbsConnector.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, order.getOrderStatus().toString());
            stmt.setString(2, order.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
