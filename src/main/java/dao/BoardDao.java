package dao;

import model.decorator.Board;

import java.util.List;

public interface BoardDao {
    Board selectBoardById(String id);
    Board selectBoardByName(String name);
    void addBoard(Board board, String idOrder);
    void addBoard(Board board);
    List<Board> selectAvailableBoards();
}

