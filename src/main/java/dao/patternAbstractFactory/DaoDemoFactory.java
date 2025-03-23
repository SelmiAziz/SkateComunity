package dao.patternAbstractFactory;


import dao.*;

public class DaoDemoFactory extends DaoFactory {


    public UserDemoDao createUserDao(){
        return UserDemoDao.getInstance();
    }

    public CustomerDemoDao createCostumerDao(){
        return CustomerDemoDao.getInstance();
    }

    public OrganizerDemoDao createOrganizerDao(){
        return OrganizerDemoDao.getInstance();
    }

    public EventDemoDao createEventDao(){return EventDemoDao.getInstance();}

    public TrickDemoDao createTrickDao(){return TrickDemoDao.getInstance();}

    public EventRegistrationDemoDao createEventRegistrationDao(){return EventRegistrationDemoDao.getInstance();}
}
