package dao;

import dao.patternabstractfactory.DaoFactory;
import exceptions.DataAccessException;
import model.*;
import model.decorator.Board;
import utils.DbsConnector;

import javax.xml.crypto.Data;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderDbmsDao implements OrderDao {
    private static OrderDbmsDao instance;
    private final List<Order> customOrderList = new ArrayList<>();
    private final DeliveryDestinationDao deliveryDestinationDao = DaoFactory.getInstance().createDeliveryDestinationDao();
    private final ProgressNoteDao progressNoteDao = DaoFactory.getInstance().createProgressNoteDao();
    private final BoardDao boardDao = DaoFactory.getInstance().createBoardDao();

    public static synchronized OrderDbmsDao getInstance(){
        if(instance == null){
            instance = new OrderDbmsDao();
        }
        return instance;
    }
    @Override
    public List<Order> selectAllOpenOrder() throws DataAccessException {
        List<Order> openCustomOrderList = new ArrayList<>();

        String sql = "SELECT o.id, " +
                "o.customerUsername, " +
                "o.boardId, " +
                "o.preferredTimeSlot, " +
                "o.status, " +
                "o.deliveryDestinationId, " +
                "GROUP_CONCAT(DISTINCT pn.id) AS progressNoteIds " +
                "FROM orders o " +
                "LEFT JOIN progressNotes pn ON pn.orderId = o.id " +
                "WHERE o.status IN ('Requested', 'Processing') " +
                "GROUP BY o.id, o.customerUsername, o.boardId, o.preferredTimeSlot, o.status, o.deliveryDestinationId";

        Connection conn = DbsConnector.getInstance().getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String id = rs.getString("id");
                String boardId = rs.getString("boardId");
                String preferredTimeSlot = rs.getString("preferredTimeSlot");
                String orderStatusStr = rs.getString("status");

                Board board = boardDao.selectBoardById(boardId);

                String deliveryDestinationId = rs.getString("deliveryDestinationId");
                DeliveryDestination deliveryDestination = deliveryDestinationDao.selectDeliveryDestinationById(deliveryDestinationId);

                String progressNoteIdsStr = rs.getString("progressNoteIds");

                DeliveryPreferences deliveryPreferences = new DeliveryPreferences("", preferredTimeSlot);
                Order order = new Order(deliveryDestination, deliveryPreferences, board);
                order.setId(id);
                order.setOrderStatus(OrderStatus.fromString(orderStatusStr));

                if (progressNoteIdsStr != null && !progressNoteIdsStr.isEmpty()) {
                    for (String progressNoteId : progressNoteIdsStr.split(",")) {
                        progressNoteId = progressNoteId.trim();
                        ProgressNote progressNote = progressNoteDao.selectProgressNoteById(progressNoteId);
                        if (progressNote != null) {
                            order.addProgressNote(progressNote);
                        }
                    }
                }

                openCustomOrderList.add(order);
            }

            return openCustomOrderList;
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }

    }





    @Override
    public void saveOrder(Order order) throws DataAccessException {
        customOrderList.add(order);
        deliveryDestinationDao.saveDeliveryDestination(order.getDeliveryDestination());

        String sql = "INSERT INTO orders (id, customerUsername, comment, preferredTimeSlot, boardId, status, deliveryDestinationId) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DbsConnector.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, order.getId());
            stmt.setString(2, order.getCustomer().getUsername());
            stmt.setString(3, order.commentOrderPreferences());
            stmt.setString(4, order.timeSlotOrderPreferences());
            stmt.setString(5, order.getBoard().boardCode());
            stmt.setString(6, order.getOrderStatus().toString());
            stmt.setString(7, order.getDeliveryDestination().getId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }

    }


    @Override
    public Order selectOrderByCode(String id) throws DataAccessException {
        for (Order order : this.customOrderList) {
            if (order.getId().equals(id)) {
                return order;
            }
        }

        String sql = "SELECT o.id, " +
                "o.customerUsername, " +
                "o.boardId, " +
                "o.preferredTimeSlot, " +
                "o.status, " +
                "o.deliveryDestinationId, " +
                "GROUP_CONCAT(DISTINCT pn.id) AS progressNoteIds " +
                "FROM orders o " +
                "LEFT JOIN progressNotes pn ON pn.orderId = o.id " +
                "WHERE o.id = ? " +
                "GROUP BY o.id, o.customerUsername, o.boardId, o.preferredTimeSlot, o.status, o.deliveryDestinationId";

        Connection conn = DbsConnector.getInstance().getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String boardId = rs.getString("boardId");
                String preferredTimeSlot = rs.getString("preferredTimeSlot");
                String orderStatusStr = rs.getString("status");

                Board board = boardDao.selectBoardById(boardId);

                String deliveryDestinationId = rs.getString("deliveryDestinationId");
                DeliveryDestination deliveryDestination = deliveryDestinationDao.selectDeliveryDestinationById(deliveryDestinationId);

                String progressNoteIdsStr = rs.getString("progressNoteIds");

                DeliveryPreferences deliveryPreferences = new DeliveryPreferences("", preferredTimeSlot);
                Order order = new Order(deliveryDestination, deliveryPreferences, board);
                order.setId(id);
                order.setOrderStatus(OrderStatus.fromString(orderStatusStr));

                if (progressNoteIdsStr != null && !progressNoteIdsStr.isEmpty()) {
                    for (String progressNoteId : progressNoteIdsStr.split(",")) {
                        progressNoteId = progressNoteId.trim();
                        ProgressNote progressNote = progressNoteDao.selectProgressNoteById(progressNoteId);
                        if (progressNote != null) {
                            order.addProgressNote(progressNote);
                        }
                    }
                }

                this.customOrderList.add(order);
                return order;
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }

        return null;
    }




    @Override
    public void updateOrder(Order order) throws DataAccessException {
        String sql = "UPDATE orders SET status = ? WHERE id = ?";

        try (Connection conn = DbsConnector.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, order.getOrderStatus().toString());
            stmt.setString(2, order.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }


}
