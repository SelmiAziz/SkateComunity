package controls;

import Dao.AccountDao;
import Dao.Factories.DaoAccountFactory;
import beans.RegisterAccountBean;
import login.Account;
import utils.DbsConnector;

public class RegisterController {
    private AccountDao accountDao = DaoAccountFactory.getInstance().createAccountDao();

    //you should change this with registerUserBean e usare al suo interno anche account
    public void registerUser(RegisterAccountBean registerAccountBean){
        if (accountDao.getAccountByUsername(registerAccountBean.getUsername()) != null){
            System.out.println("Sei già registrato controlla meglio");
            return;
        }
        Account account = new Account(registerAccountBean.getUsername(),registerAccountBean.getPassword(), registerAccountBean.getAccountType());
        accountDao.addAccount(account);
    }

}
