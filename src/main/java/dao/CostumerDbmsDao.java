package dao;

import model.Costumer;
import utils.DbsConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CostumerDbmsDao implements CostumerDao{
    private static CostumerDbmsDao instance;
    private final List<Costumer> costumerList = new ArrayList<>();

    public static synchronized CostumerDbmsDao getInstance(){
        if(instance == null){
            instance = new CostumerDbmsDao();
        }
        return instance;
    }

    @Override
    public Costumer selectCostumerByCostumerName(String  profileName) {
        for(Costumer costumer:costumerList){
            if(costumer.getName().equals(profileName)){
                return costumer;
            }
        }
        String query = "SELECT profileName, numCoins FROM profiles WHERE profileName = ?";
        Connection connection = DbsConnector.getInstance().getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, profileName);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String pName = resultSet.getString("profileName");
                    int numCoins = resultSet.getInt("numCoins");
                    Costumer costumer = new  Costumer(pName, numCoins);
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
    public void addCostumer(Costumer costumer) {
        costumerList.add(costumer);

        String query = "INSERT INTO profiles (profileName, numCoins, profileType) VALUES (?, ?, ?)";
        Connection connection = DbsConnector.getInstance().getConnection();
        String profileName = costumer.getName();
        int numCoins = costumer.getCoins();
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, profileName);
            preparedStatement.setInt(2, numCoins);
            preparedStatement.setString(3, "costumer");
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Costumer costumer) {
        String query = "UPDATE profiles SET numCoins = ? WHERE profileName = ?";
        Connection connection = DbsConnector.getInstance().getConnection();

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, costumer.getCoins());
            preparedStatement.setString(2, costumer.getName());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
