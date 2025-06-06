package dao.patternabstractfactory;

import dao.*;

public class DaoFileSystemFactory extends DaoFactory {
    public CustomerDao createCustomerDao(){
        return CustomerFileSystemDao.getInstance();
    }

    public WalletDao createWalletDao(){return WalletFileSystemDao.getInstance();}

    public UserDao createUserDao(){
        return UserFileSystemDao.getInstance();
    }

    public CompetitionDao createCompetitionDao(){return null;}

    public OrganizerDao createOrganizerDao(){return null;}


    public TrickDao createTrickDao(){return null;}

    public RegistrationDao createRegistrationDao(){return null;}

    public BoardDao createBoardDao(){return null;}

    public OrderDao createCustomOrderDao(){return null;}

    public ProgressNoteDao createProgressNoteDao(){return null;}

    public DeliveryDestinationDao createDeliveryDestinationDao(){
        return null;
    }
}
