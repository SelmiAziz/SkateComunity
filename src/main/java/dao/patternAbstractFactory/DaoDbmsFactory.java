package Dao.patternAbstractFactory;

import Dao.*;

public class DaoDbmsFactory extends DaoFactory {



    public UserDbmsDao createUserDao(){
        return UserDbmsDao.getInstance();
    }

    public CostumerDbmsDao createCostumerDao(){
        return CostumerDbmsDao.getInstance();
    }

    public OrganizerDbmsDao createOrganizerDao(){
        return OrganizerDbmsDao.getInstance();
    }

    public EventDbmsDao createEventDao(){ return EventDbmsDao.getInstance();}

    public EventRegistrationDbmsDao createEventRegistrationDao(){return EventRegistrationDbmsDao.getInstance();};
}
