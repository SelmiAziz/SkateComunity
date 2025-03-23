package controls;

import beans.TrickBean;
import dao.TrickDao;
import dao.patternAbstractFactory.DaoFactory;
import model.Trick;
import utils.DifficultyTrick;

import java.util.ArrayList;
import java.util.List;

public class LearnTrickController {
    TrickDao trickDao = DaoFactory.getInstance().createTrickDao();

    public List<TrickBean> allAvailableTricksDetailed(){
        List<Trick> availableTricks = trickDao.selectAvailableTricks();
        List<TrickBean> availableTricksBean = new ArrayList<>();
        for(Trick trick:availableTricks){
            String difficulty = trick.getDifficultyTrick().name();
            TrickBean trickBean = new TrickBean(trick.getNameTrick(), trick.getDescription(), difficulty, trick.getCategory() );
            availableTricksBean.add(trickBean);
        }
        return availableTricksBean;
    }


    public List<TrickBean> allAvailableTricks(){
        List<Trick> availableTricks = trickDao.selectAvailableTricks();
        List<TrickBean> availableTricksBean = new ArrayList<>();
        for(Trick trick:availableTricks){
            TrickBean trickBean = new TrickBean(trick.getNameTrick() );
            availableTricksBean.add(trickBean);
        }
        return availableTricksBean;
    }



    public void RegisterTrick(TrickBean trickBean){
        DifficultyTrick difficulty = DifficultyTrick.fromString(trickBean.getDifficulty());
        System.out.println("La dfficoltà"+trickBean.getDifficulty());
        Trick trick = new Trick(trickBean.getNameTrick(), trickBean.getDescription(), difficulty, trickBean.getCategory(), trickBean.getDate());
        trickDao.addTrick(trick);
    }


    public TrickBean detailsTrick(TrickBean trickBean){
        Trick trick = trickDao.selectTrickByName(trickBean.getNameTrick());
        String difficulty = trick.getDifficultyTrick().name();
        TrickBean trickDetailedBean = new TrickBean(trick.getNameTrick(), trick.getDescription(), difficulty, trick.getCategory());
        return trickDetailedBean;
    }
}
