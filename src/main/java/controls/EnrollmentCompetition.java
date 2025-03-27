package controls;

import dao.patternAbstractFactory.DaoFactory;
import dao.UserDao;

public class EnrollmentCompetition {
    private  final UserDao userDao = DaoFactory.getInstance().createUserDao();




}
