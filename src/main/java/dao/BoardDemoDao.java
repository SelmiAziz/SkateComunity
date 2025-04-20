package dao;

import model.decorator.Board;

import java.util.ArrayList;
import java.util.List;

public class BoardDemoDao implements BoardDao {
    List<Board> boardListBase = new ArrayList<>();
    List<Board> boardList = new ArrayList<>();

    private static BoardDemoDao instance;

    public static synchronized BoardDemoDao getInstance(){
        if(instance == null){
            instance = new BoardDemoDao();
        }
        return instance;
    }

    public void addBoard(Board board){
        boardListBase.add(board);
    }

    @Override
    public List<Board> selectAvailableBoards() {
        return boardListBase;
    }

    @Override
    public Board selectBoardByName(String name) {
        for(Board board : boardList){
            if(board.name().equals(name)){
                return board;
            }
        }
        return null;
    }

    @Override
    public Board selectBoardById(String id) {
        for(Board board : boardList){
            if(board.boardId().equals(id)){
                return board;
            }
        }
        return null;
    }

    @Override
    public void addBoard(Board board, String customerId) {
        this.boardList.add(board);
    }
}
