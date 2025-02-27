package Dao.Factories;

import Dao.AccountDao;

abstract public class DaoAccountFactory {
    public abstract AccountDao createAccountDao();

    private static DaoAccountDemoFactory instance;

    public synchronized static DaoAccountDemoFactory getInstance(){

            instance = new DaoAccountDemoFactory();
            return instance;
    }
}
