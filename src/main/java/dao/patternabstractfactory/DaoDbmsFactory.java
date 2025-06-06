package dao.patternabstractfactory;

import dao.*;

public class DaoDbmsFactory extends DaoFactory {

    public WalletDao createWalletDao(){return WalletDbmsDao.getInstance();}

    public UserDao createUserDao(){
        return UserDbmsDao.getInstance();
    }

    public CustomerDao createCustomerDao(){
        return CustomerDbmsDao.getInstance();
    }

    public OrganizerDao createOrganizerDao(){
        return OrganizerDbmsDao.getInstance();
    }

    public CompetitionDao createCompetitionDao(){ return CompetitionDbmsDao.getInstance();}

    public RegistrationDao createRegistrationDao(){return RegistrationDbmsDao.getInstance();}

    public TrickDao createTrickDao(){return TrickDbmsDao.getInstance();}

    public BoardDao createBoardDao(){return BoardDbmsDao.getInstance();}

    public OrderDao createCustomOrderDao(){return OrderDbmsDao.getInstance();}

    public ProgressNoteDao createProgressNoteDao(){return ProgressNoteDbmsDao.getInstance();}

    public DeliveryDestinationDao createDeliveryDestinationDao(){return DeliveryDestinationDbmsDao.getInstance();}
}
