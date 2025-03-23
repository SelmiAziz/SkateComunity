package dao.patternAbstractFactory;

import dao.*;

abstract public class DaoFactory {
    public abstract UserDao createUserDao();
    public abstract CustomerDao createCostumerDao();
    public abstract OrganizerDao createOrganizerDao();
    public abstract EventDao createEventDao();
    public abstract EventRegistrationDao createEventRegistrationDao();
    public abstract TrickDao createTrickDao();

    private static DaoFactory instance;

    public synchronized static DaoFactory getInstance(){
            if(instance == null){

                instance = new DaoDbmsFactory();
            }
            return instance;
    }
}
