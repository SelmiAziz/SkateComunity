package Dao.patternAbstractFactory;

import Dao.*;

abstract public class DaoFactory {
    public abstract UserDao createUserDao();
    public abstract CostumerDao createCostumerDao();
    public abstract OrganizerDao createOrganizerDao();
    public abstract EventDao createEventDao();
    public abstract EventRegistrationDao createEventRegistrationDao();

    private static DaoFactory instance;

    public synchronized static DaoFactory getInstance(){
            if(instance == null){

                instance = new DaoDbmsFactory();
            }
            return instance;
    }
}
