package dao;

import model.BoardBase;
import model.decorator.Board;
import utils.DbsConnector;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BoardDbmsDao implements BoardDao {
    List<Board> boardList = new ArrayList<>();
    List<Board> boardListBase = new ArrayList<>();
    private static BoardDbmsDao instance;

    public static synchronized BoardDbmsDao getInstance(){
        if(instance == null){
            instance = new BoardDbmsDao();
        }
        return instance;
    }

    @Override
    public List<Board> selectAvailableBoards() {
        if(!this.boardListBase.isEmpty()) {
            return this.boardListBase;
        }
        String sql = "SELECT id, name, description, size, price FROM boardSamples";

        Connection conn = DbsConnector.getInstance().getConnection();

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Board board = new BoardBase(
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getString("size"),
                        rs.getInt("price")
                );

                this.boardListBase.add(board);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return this.boardListBase;
    }

    @Override
    public void addBoard(Board board){
        boardListBase.add(board);

        String sql = "INSERT INTO boardSamples (id, name, description, size, price) " +
                "VALUES (?, ?, ?, ?, ?)";

        Connection conn = DbsConnector.getInstance().getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, board.boardId());
            stmt.setString(2, board.name());
            stmt.setString(3, board.description());
            stmt.setString(4, board.size());
            stmt.setInt(5, board.price());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addBoard(Board board, String idOrder) {
        boardList.add(board);
    }

    @Override
    public Board selectBoardById(String id) {
        for(Board board: boardList){
            if(board.boardId().equals(id)){
                return board;
            }
        }

        return null;
    }

    @Override
    public Board selectBoardByName(String name) {
        for(Board board: boardListBase){
            if(board.name().equals(name)){
                return board;
            }
        }
        return null;
    }
}
