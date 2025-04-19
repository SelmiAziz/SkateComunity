package dao.patternAbstractFactory;


import dao.*;

public class DaoDemoFactory extends DaoFactory {

    public WalletDemoDao createWalletDao(){return WalletDemoDao.getInstance();}

    public UserDemoDao createUserDao(){
        return UserDemoDao.getInstance();
    }

    public CustomerDemoDao createCostumerDao(){
        return CustomerDemoDao.getInstance();
    }

    public OrganizerDemoDao createOrganizerDao(){
        return OrganizerDemoDao.getInstance();
    }

    public CompetitionDemoDao createCompetitionDao(){return CompetitionDemoDao.getInstance();}

    public TrickDemoDao createTrickDao(){return TrickDemoDao.getInstance();}

    public CompetitionRegistrationDemoDao createCompetitionRegistrationDao(){return CompetitionRegistrationDemoDao.getInstance();}

    public BoardDemoDao createBoardDao(){return BoardDemoDao.getInstance();}

    public CustomOrderDao createCustomOderDao(){return CustomOrderDemoDao.getInstance();}

    public ProgressNoteDao createProgressNoteDao(){return ProgressNoteDemoDao.getInstance();}

    public DeliveryDestinationDao deliveryDestinationDao(){return DeliveryDestinationDemoDao.getInstance();}
}
