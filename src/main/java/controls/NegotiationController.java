package controls;

import dao.patternAbstractFactory.DaoFactory;
import dao.UserDao;
import beans.UserInfo;
import login.User;
import utils.SessionManager;

public class NegotiationController {
    private  final UserDao userDao = DaoFactory.getInstance().createUserDao();

    public void publicNegotiation(){

    }

    public void applyForANegotiation(){

    }

    public void showNegotiationCostumer(){

    }

    public void showNegotiationOrganizer(){
        
    }

    public UserInfo getCurrentUserInfo(){
        User user = userDao.selectUserByUsername(SessionManager.getInstance().getSession().getUsername());
        return new  UserInfo(user.getUsername(), user.getProfile().getCoins());
    }

}
