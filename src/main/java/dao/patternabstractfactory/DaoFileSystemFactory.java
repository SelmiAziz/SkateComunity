package dao.patternabstractfactory;

import dao.*;

public class DaoFileSystemFactory {
    public CustomerFileSystemDao createCostumerDao(){
        return CustomerFileSystemDao.getInstance();
    }

    public WalletFileSystemDao createWalletDao(){return WalletFileSystemDao.getInstance();}

    public UserFileSystemDao createUserDao(){
        return UserFileSystemDao.getInstance();
    }
}
