package Dao.patternAbstractFactory;


import Dao.*;

public class DaoDemoFactory extends DaoFactory {


    public UserDemoDao createUserDao(){
        return UserDemoDao.getInstance();
    }

    public CostumerDemoDao createCostumerDao(){
        return CostumerDemoDao.getInstance();
    }

    public OrganizerDemoDao createOrganizerDao(){
        return OrganizerDemoDao.getInstance();
    }

    public EventDemoDao createEventDao(){return EventDemoDao.getInstance();}


    public EventRegistrationDemoDao createEventRegistrationDao(){return EventRegistrationDemoDao.getInstance();}
}
