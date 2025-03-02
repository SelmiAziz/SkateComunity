package Dao.Factories;


import Dao.AccountDemoDao;

public class DaoAccountDemoFactory extends DaoAccountFactory {
    public AccountDemoDao createAccountDao(){
        return AccountDemoDao.getInstance();
    }
}
