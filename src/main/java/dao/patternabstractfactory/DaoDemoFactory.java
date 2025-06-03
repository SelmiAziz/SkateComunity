package dao.patternabstractfactory;


import dao.*;

public class DaoDemoFactory extends DaoFactory {

    public WalletDao createWalletDao(){return WalletDemoDao.getInstance();}

    public UserDao createUserDao(){
        return UserDemoDao.getInstance();
    }

    public CustomerDao createCostumerDao(){
        return CustomerDemoDao.getInstance();
    }

    public OrganizerDao createOrganizerDao(){
        return OrganizerDemoDao.getInstance();
    }

    public CompetitionDao createCompetitionDao(){return CompetitionDemoDao.getInstance();}

    public TrickDao createTrickDao(){return TrickDemoDao.getInstance();}

    public RegistrationDao createRegistrationDao(){return RegistrationDemoDao.getInstance();}

    public BoardDao createBoardDao(){return BoardDemoDao.getInstance();}

    public OrderDao createCustomOrderDao(){return OrderDemoDao.getInstance();}

    public ProgressNoteDao createProgressNoteDao(){return ProgressNoteDemoDao.getInstance();}

    public DeliveryDestinationDao createDeliveryDestinationDao(){
        return null;
    }
}
