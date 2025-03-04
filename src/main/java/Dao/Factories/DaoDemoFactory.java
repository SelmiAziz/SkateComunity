package Dao.Factories;


import Dao.*;

public class DaoDemoFactory extends DaoFactory {
    public AccountDemoDao createAccountDao(){
        return AccountDemoDao.getInstance();
    }

    public CostumerDao createCostumerDao(){
        return CostumerDemoDao.getInstance();
    }

    public OrganizerDao createOrganizerDao(){
        return OrganizerDemoDao.getInstance();
    }

    public EventDao createEventDao(){return EventDemoDao.getInstance();}
}
