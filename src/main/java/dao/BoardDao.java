package dao;

import model.decorator.Board;

import java.util.List;

public interface BoardDao {
    Board selectBoardById(String boardCode);
    Board selectBoardByName(String name);
    void addBoard(Board board, String customerUsername);
    void addBoard(Board board);
    List<Board> selectAvailableBoards();
}

