package dao;

import exceptions.DataAccessException;
import model.decorator.Board;

import java.util.List;

public interface BoardDao {
    Board selectBoardById(String boardCode) throws DataAccessException;
    Board selectBoardByName(String name) throws DataAccessException;
    void addBoard(Board board, String customerUsername) throws DataAccessException;
    void addBoard(Board board) throws DataAccessException;
    List<Board> selectAvailableBoards() throws DataAccessException;
}

