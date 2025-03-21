package controls;

import dao.patternAbstractFactory.DaoFactory;
import dao.UserDao;
import beans.UserInfo;
import login.User;
import utils.SessionManager;

public class EnrollmentCompetition {
    private  final UserDao userDao = DaoFactory.getInstance().createUserDao();


    public UserInfo getCurrentUserInfo(){
        User user = userDao.selectUserByUsername(SessionManager.getInstance().getSession().getUsername());
        return new  UserInfo(user.getUsername(), user.getProfile().getCoins());
    }

}
