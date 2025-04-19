package dao.patternAbstractFactory;

import dao.*;

abstract public class DaoFactory {
    public abstract WalletDao createWalletDao();
    public abstract UserDao createUserDao();
    public abstract CustomerDao createCostumerDao();
    public abstract OrganizerDao createOrganizerDao();
    public abstract CompetitionDao createCompetitionDao();
    public abstract CompetitionRegistrationDao createCompetitionRegistrationDao();
    public abstract TrickDao createTrickDao();
    public abstract BoardDao createBoardDao();
    public abstract CustomOrderDao createCustomOrderDao();
    public abstract ProgressNoteDao createProgressNoteDao();
    public abstract DeliveryDestinationDao createDeliveryDestinationDao();

    private static DaoFactory instance;

    public synchronized static DaoFactory getInstance(){
            if(instance == null){

                instance = new DaoDbmsFactory();
            }
            return instance;
    }
}
