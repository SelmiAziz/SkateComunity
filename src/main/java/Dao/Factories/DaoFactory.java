package Dao.Factories;

import Dao.AccountDao;

abstract public class DaoAccountFactory {
    public abstract AccountDao createAccountDao();

    private static DaoAccountFactory instance;

    public synchronized static DaoAccountFactory getInstance(){
            //For now it is working in this way
            if(instance == null){

                instance = new DaoAccountDemoFactory();
            }
            return instance;
    }
}
