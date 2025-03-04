package Dao.Factories;

import Dao.AccountDao;
import Dao.CostumerDao;
import Dao.EventDao;
import Dao.OrganizerDao;

abstract public class DaoFactory {
    public abstract AccountDao createAccountDao();
    public abstract CostumerDao createCostumerDao();
    public abstract OrganizerDao createOrganizerDao();
    public abstract EventDao createEventDao();

    private static DaoFactory instance;

    public synchronized static DaoFactory getInstance(){
            //For now it is working in this way
            if(instance == null){

                instance = new DaoDemoFactory();
            }
            return instance;
    }
}
