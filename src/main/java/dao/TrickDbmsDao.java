package dao;

import model.Trick;
import utils.DbsConnector;
import utils.DifficultyTrick;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TrickDbmsDao implements TrickDao{
    private static TrickDbmsDao instance;
    private List<Trick> trickList = new ArrayList<>();

    public static synchronized TrickDbmsDao getInstance(){
        if(instance == null){
            instance = new TrickDbmsDao();
        }
        return instance;
    }


    @Override
    public void addTrick(Trick trick) {
        this.trickList.add(trick);

        String sql = "INSERT INTO tricks (trickName, description, difficulty, category, date) " +
                "VALUES (?, ?, ?, ?, ?)";

        Connection conn = DbsConnector.getInstance().getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, trick.getNameTrick());
            stmt.setString(2, trick.getDescription());
            stmt.setString(3, trick.getDifficultyTrick().toString()); // Se `Difficulty` è un Enum
            stmt.setString(4, trick.getCategory());
            stmt.setString(5, trick.getDataCaricamento());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public Trick selectTrickByName(String trickName) {
        for(Trick trick:this.trickList){
            if(trick.equals(trickName)){
                return trick;
            }
        }
        String sql = "SELECT trickName, description, difficulty, category, date " +
                "FROM tricks " +
                "WHERE trickName = ?";

        Connection conn = DbsConnector.getInstance().getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, trickName);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Trick trick = new Trick(
                        rs.getString("trickName"),
                        rs.getString("description"),
                        DifficultyTrick.valueOf(rs.getString("difficulty")),
                        rs.getString("category"),
                        rs.getString("date")
                );

                return trick;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;

    }

    @Override
    public List<Trick> selectAvailableTricks(){
        if(!this.trickList.isEmpty()) {
            return this.trickList;
        }
        List<Trick> newTrickList = new ArrayList<>();
        String sql = "SELECT trickName, description, difficulty, category, date FROM tricks";

        Connection conn = DbsConnector.getInstance().getConnection();

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Trick trick = new Trick(
                        rs.getString("trickName"),
                        rs.getString("description"),
                        DifficultyTrick.valueOf(rs.getString("difficulty")),
                        rs.getString("category"),
                        rs.getString("date")
                );

                this.trickList.add(trick);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return this.trickList;

    }
}
