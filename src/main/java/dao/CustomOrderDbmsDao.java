package dao;

import dao.patternAbstractFactory.DaoFactory;
import model.Order;
import model.OrderStatus;
import utils.DbsConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomOrderDbmsDao implements CustomOrderDao {
    private static CustomOrderDbmsDao instance;
    private final List<Order> customOrderList = new ArrayList<>();
    private final DeliveryDestinationDao deliveryDestinationDao = DaoFactory.getInstance().createDeliveryDestinationDao();

    public static synchronized CustomOrderDbmsDao getInstance(){
        if(instance == null){
            instance = new CustomOrderDbmsDao();
        }
        return instance;
    }

    @Override
    public Order selectCustomOrderById(String id) {
        for(Order customOrder: this.customOrderList){
            if(customOrder.getId().equals(id)){
                return customOrder;
            }
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
        for(Order customOrder:this.customOrderList){
            if(customOrder.getOrderStatus() == OrderStatus.REQUESTED || customOrder.getOrderStatus() == OrderStatus.PROCESSING){
                openCustomOrderList.add(customOrder);
            }
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
