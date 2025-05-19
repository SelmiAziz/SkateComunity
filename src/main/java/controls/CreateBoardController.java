package controls;

import beans.BoardProfileBean;
import dao.BoardDao;
import dao.patternabstractfactory.DaoFactory;
import exceptions.SessionExpiredException;
import model.BoardBase;
import model.decorator.Board;
import utils.Session;
import utils.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class CreateBoardController {
    private final BoardDao boardDao = DaoFactory.getInstance().createBoardDao();

    public void createBoard(String token, BoardProfileBean boardBean) throws SessionExpiredException {
        Session session = SessionManager.getInstance().getSessionByToken(token);
        if(session == null){
            throw new SessionExpiredException();
        }
        Board board = new BoardBase(boardBean.getName(), boardBean.getDescription(), boardBean.getSize(), boardBean.getPrice());
        boardDao.addBoard(board);
    }

    public List<BoardProfileBean> getStoredBoards(String token) throws SessionExpiredException  {
        Session session = SessionManager.getInstance().getSessionByToken(token);
        if(session == null){
            throw new SessionExpiredException();
        }
        List<BoardProfileBean> boardBeanList = new ArrayList<>();
        List<Board> boardList = boardDao.selectAvailableBoards();
        for (Board board : boardList) {
            BoardProfileBean boardBean = new BoardProfileBean(board.name(), board.description(), board.size(), board.price());
            boardBeanList.add(boardBean);
        }
        return boardBeanList;
    }

}
