package controls;

import beans.TrickBean;
import dao.TrickDao;
import dao.patternabstractfactory.DaoFactory;
import exceptions.DataAccessException;
import exceptions.SessionExpiredException;
import model.Trick;
import utils.DifficultyTrick;
import utils.Session;
import utils.SessionManager;

import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.List;

public class LearnTrickController {
    TrickDao trickDao = DaoFactory.getInstance().createTrickDao();

    public List<TrickBean> allAvailableTricksDetailed(String token) throws SessionExpiredException, DataAccessException{
        Session session = SessionManager.getInstance().getSessionByToken(token);
        if(session == null){
            throw new SessionExpiredException();
        }
        List<Trick> availableTricks = trickDao.selectAvailableTricks();
        List<TrickBean> availableTricksBean = new ArrayList<>();
        for(Trick trick:availableTricks){
            String difficulty = trick.getDifficultyTrick().name();
            TrickBean trickBean = new TrickBean(trick.getNameTrick(), trick.getDescription(), difficulty, trick.getCategory() );
            availableTricksBean.add(trickBean);
        }
        return availableTricksBean;
    }


    public List<TrickBean> allAvailableTricks(String token) throws SessionExpiredException, DataAccessException{
        Session session = SessionManager.getInstance().getSessionByToken(token);
        if(session == null){
            throw new SessionExpiredException();
        }
        List<Trick> availableTricks = trickDao.selectAvailableTricks();
        List<TrickBean> availableTricksBean = new ArrayList<>();
        for(Trick trick:availableTricks){
            TrickBean trickBean = new TrickBean(trick.getNameTrick() );
            availableTricksBean.add(trickBean);
        }
        return availableTricksBean;
    }



    public void registerTrick(String token, TrickBean trickBean) throws SessionExpiredException, DataAccessException {
        Session session = SessionManager.getInstance().getSessionByToken(token);
        if(session == null){
            throw new SessionExpiredException();
        }
        DifficultyTrick difficulty = DifficultyTrick.fromString(trickBean.getDifficulty());
        Trick trick = new Trick(trickBean.getNameTrick(), trickBean.getDescription(), difficulty, trickBean.getCategory(), trickBean.getDate());
        trickDao.addTrick(trick);
    }


    public TrickBean detailsTrick(String token ,TrickBean trickBean) throws SessionExpiredException, DataAccessException {
        Session session = SessionManager.getInstance().getSessionByToken(token);
        if(session == null){
            throw new SessionExpiredException();
        }
        Trick trick = trickDao.selectTrickByName(trickBean.getNameTrick());
        String difficulty = trick.getDifficultyTrick().name();
        return new TrickBean(trick.getNameTrick(), trick.getDescription(), difficulty, trick.getCategory());
    }
}
