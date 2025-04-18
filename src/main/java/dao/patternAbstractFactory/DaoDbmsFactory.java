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

    public CompetitionDbmsDao createCompetitionDao(){ return CompetitionDbmsDao.getInstance();}

    public CompetitionRegistrationDbmsDao createCompetitionRegistrationDao(){return CompetitionRegistrationDbmsDao.getInstance();};

    public TrickDbmsDao createTrickDao(){return TrickDbmsDao.getInstance();}

    public BoardDbmsDao createBoardDao(){return BoardDbmsDao.getInstance();}

    public CustomOrderDao createCustomOderDao(){return CustomOrderDbmsDao.getInstance();}

    public ProgressNoteDao createProgressNoteDao(){return ProgressNoteDbmsDao.getInstance();}

    public DeliveryDestinationDao deliveryDestinationDao(){return DeliveryDestinationDbmsDao.getInstance();}
}
