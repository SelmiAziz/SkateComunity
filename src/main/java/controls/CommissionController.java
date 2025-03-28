package controls;

import dao.patternAbstractFactory.DaoFactory;
import dao.UserDao;

public class CommissionController {
    private  final UserDao userDao = DaoFactory.getInstance().createUserDao();




}
