package dao.patternAbstractFactory;

import dao.*;

public class DaoDbmsFactory extends DaoFactory {

    public WalletDbmsDao createWalletDao(){return WalletDbmsDao.getInstance();}

    public UserDbmsDao createUserDao(){
        return UserDbmsDao.getInstance();
    }

    public CustomerDbmsDao createCostumerDao(){
        return CustomerDbmsDao.getInstance();
    }

    public OrganizerDbmsDao createOrganizerDao(){
        return OrganizerDbmsDao.getInstance();
    }

    public EventDbmsDao createEventDao(){ return EventDbmsDao.getInstance();}

    public EventRegistrationDbmsDao createEventRegistrationDao(){return EventRegistrationDbmsDao.getInstance();};

    public TrickDbmsDao createTrickDao(){return TrickDbmsDao.getInstance();}
}
