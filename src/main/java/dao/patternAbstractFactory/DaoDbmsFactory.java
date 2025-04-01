package dao.patternAbstractFactory;

import dao.*;

public class DaoDbmsFactory extends DaoFactory {

    public WalletDao createWalletDao(){return WalletFileSystemDao.getInstance();}

    public UserDao createUserDao(){
        return UserFileSystemDao.getInstance();
    }

    public CustomerDao createCostumerDao(){
        return CustomerFileSystemDao.getInstance();
    }

    public OrganizerDbmsDao createOrganizerDao(){
        return OrganizerDbmsDao.getInstance();
    }

    public CompetitionDbmsDao createCompetitionDao(){ return CompetitionDbmsDao.getInstance();}

    public CompetitionRegistrationDbmsDao createCompetitionRegistrationDao(){return CompetitionRegistrationDbmsDao.getInstance();};

    public TrickDbmsDao createTrickDao(){return TrickDbmsDao.getInstance();}
}
