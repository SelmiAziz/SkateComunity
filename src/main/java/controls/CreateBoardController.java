package controls;

import beans.CustomizedBoardBean;
import dao.BoardDao;
import dao.patternAbstractFactory.DaoFactory;
import model.BoardBase;
import model.decorator.Board;

import java.util.ArrayList;
import java.util.List;

public class CreateBoardController {
    private final BoardDao boardDao = DaoFactory.getInstance().createBoardDao();

    public void createBoard(CustomizedBoardBean boardBean) {
        Board board = new BoardBase(boardBean.getName(), boardBean.getDescription(), boardBean.getSize(), boardBean.getPrice());
        boardDao.addBoard(board);
    }

    public List<CustomizedBoardBean> getStoredBoards() {
        List<CustomizedBoardBean> boardBeanList = new ArrayList<>();
        List<Board> boardList = boardDao.selectAvailableBoards();
        for (Board board : boardList) {
            CustomizedBoardBean boardBean = new CustomizedBoardBean(board.name(), board.description(), board.size(), board.price());
            boardBeanList.add(boardBean);
        }
        return boardBeanList;
    }

}
