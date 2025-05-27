package dao;

import exceptions.DataAccessException;
import model.DeliveryDestination;
import model.Region;
import utils.DbsConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DeliveryDestinationDbmsDao implements DeliveryDestinationDao {

    private static DeliveryDestinationDbmsDao instance;
    private final List<DeliveryDestination> deliveryDestinationList = new ArrayList<>();


    public static synchronized DeliveryDestinationDbmsDao getInstance(){
        if(instance == null){
            instance = new DeliveryDestinationDbmsDao();
        }
        return instance;
    }

    @Override
    public DeliveryDestination selectDeliveryDestinationById(String id)throws DataAccessException {
        for (DeliveryDestination destination : this.deliveryDestinationList) {
            if (destination.getId().equals(id)) {
                return destination;
            }
        }

        String query = "SELECT id, region, province, city, streetAddress " +
                "FROM deliveryDestinations " +
                "WHERE id = ?";

        Connection connection = DbsConnector.getInstance().getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String destinationId = resultSet.getString("id");
                    String region = resultSet.getString("region");
                    String province = resultSet.getString("province");
                    String city = resultSet.getString("city");
                    String streetAddress = resultSet.getString("streetAddress");

                    DeliveryDestination deliveryDestination = new DeliveryDestination(destinationId, Region.fromString(region), province, city, streetAddress);
                    this.deliveryDestinationList.add(deliveryDestination);
                    return deliveryDestination;
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
        return null;
    }

    @Override
    public void saveDeliveryDestination(DeliveryDestination deliveryDestination) throws DataAccessException {
        for (DeliveryDestination destination : this.deliveryDestinationList) {
            if (destination.getId().equals(deliveryDestination.getId())) {
                return;
            }
        }

        String query = "INSERT INTO deliveryDestinations (id, region, province, city, streetAddress) " +
                "VALUES (?, ?, ?, ?, ?)";

        Connection connection = DbsConnector.getInstance().getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, deliveryDestination.getId());
            preparedStatement.setString(2, deliveryDestination.getRegion().toString());
            preparedStatement.setString(3, deliveryDestination.getProvince());
            preparedStatement.setString(4, deliveryDestination.getCity());
            preparedStatement.setString(5, deliveryDestination.getStreetAddress());
            preparedStatement.executeUpdate();

            this.deliveryDestinationList.add(deliveryDestination);
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

}
