package dao;

import model.SkateboardBase;
import model.decorator.Skateboard;
import utils.DbsConnector;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SkateboardDbmsDao implements SkateboardDao{
    List<Skateboard> skateboardList = new ArrayList<>();
    List<Skateboard> skateboardListBase = new ArrayList<>();
    private static SkateboardDbmsDao instance;

    public static synchronized  SkateboardDbmsDao getInstance(){
        if(instance == null){
            instance = new SkateboardDbmsDao();
        }
        return instance;
    }

    @Override
    public List<Skateboard> selectAvailableSkateboards() {
        if(!this.skateboardListBase.isEmpty()) {
            return this.skateboardListBase;
        }
        String sql = "SELECT id, name, description, size, price FROM skateboardSamples";

        Connection conn = DbsConnector.getInstance().getConnection();

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Skateboard skateboard = new SkateboardBase(
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getString("size"),
                        rs.getInt("price")
                );

                this.skateboardListBase.add(skateboard);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return this.skateboardListBase;

    }


    @Override
    public void addSkateboard(Skateboard skateboard){
        skateboardListBase.add(skateboard);


        String sql = "INSERT INTO skateboardSamples (id, name, description, size, price) " +
                "VALUES (?, ?, ?, ?, ?)";

        Connection conn = DbsConnector.getInstance().getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, skateboard.skateboardId());
            stmt.setString(2, skateboard.name());
            stmt.setString(3, skateboard.description());
            stmt.setString(4, skateboard.size());
            stmt.setInt(5, skateboard.price());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addSkateboard(Skateboard skateboard, String idOrder) {
        skateboardList.add(skateboard);
    }

    @Override
    public Skateboard selectSkateboardById(String id) {
        for(Skateboard skateboard: skateboardList){
            if(skateboard.skateboardId().equals(id)){
                return skateboard;
            }
        }

        return null;
    }

    @Override
    public Skateboard selectSkateboardByName(String name) {
        for(Skateboard skateboard: skateboardListBase){
            if(skateboard.name().equals(name)){
                return skateboard;
            }
        }
        return null;
    }
}
