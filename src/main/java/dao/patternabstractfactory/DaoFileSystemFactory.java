package dao.patternabstractfactory;

import dao.*;

public class DaoFileSystemFactory {
    public CustomerDao createCostumerDao(){
        return CustomerFileSystemDao.getInstance();
    }

    public WalletDao createWalletDao(){return WalletFileSystemDao.getInstance();}

    public UserDao createUserDao(){
        return UserFileSystemDao.getInstance();
    }
}
