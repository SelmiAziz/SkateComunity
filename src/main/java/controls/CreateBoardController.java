package controls;

import beans.AuthTokenBean;
import beans.CustomizedBoardBean;
import dao.BoardDao;
import dao.patternAbstractFactory.DaoFactory;
import exceptions.SessionExpiredException;
import model.BoardBase;
import model.decorator.Board;
import utils.Session;
import utils.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class CreateBoardController {
    private final BoardDao boardDao = DaoFactory.getInstance().createBoardDao();

    public void createBoard(CustomizedBoardBean boardBean, AuthTokenBean authTokenBean) throws SessionExpiredException {
        Session session = SessionManager.getInstance().getSessionByToken(authTokenBean.getToken());
        if(session == null){
            throw new SessionExpiredException();
        }
        Board board = new BoardBase(boardBean.getName(), boardBean.getDescription(), boardBean.getSize(), boardBean.getPrice());
        boardDao.addBoard(board);
    }

    public List<CustomizedBoardBean> getStoredBoards(AuthTokenBean authTokenBean) throws SessionExpiredException  {
        Session session = SessionManager.getInstance().getSessionByToken(authTokenBean.getToken());
        if(session == null){
            throw new SessionExpiredException();
        }
        List<CustomizedBoardBean> boardBeanList = new ArrayList<>();
        List<Board> boardList = boardDao.selectAvailableBoards();
        for (Board board : boardList) {
            CustomizedBoardBean boardBean = new CustomizedBoardBean(board.name(), board.description(), board.size(), board.price());
            boardBeanList.add(boardBean);
        }
        return boardBeanList;
    }

}
