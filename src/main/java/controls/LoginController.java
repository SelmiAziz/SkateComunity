package controls;

import Dao.AccountDao;
import Dao.CostumerDao;
import Dao.Factories.DaoFactory;
import Dao.OrganizerDao;
import beans.LogUserBean;
import beans.RegisterUserBean;
import exceptions.UserAlreadyExistsException;
import exceptions.UserNotFoundException;
import login.Account;
import login.AccountType;
import model.Costumer;
import model.Organizer;
import utils.*;

import java.util.List;

public class LoginController {
    private CostumerDao costumerDao = DaoFactory.getInstance().createCostumerDao();
    private AccountDao accountDao = DaoFactory.getInstance().createAccountDao();
    private OrganizerDao organizerDao = DaoFactory.getInstance().createOrganizerDao();

    public void logUser(LogUserBean loginBean) throws  UserNotFoundException{
        List<Account> accountList = accountDao.getAllAccounts();
        boolean f = false;
        for(Account account : accountList){
            if(account.getUsername().equals(loginBean.getUsername()) && account.getPassword().equals(loginBean.getPassword()) && account.getAccountType() == loginBean.getAccountType()){
                SessionManager.getInstance().createSession(new Session(account));
                AccountInfoSessionManager.getInstance().createAccountInfoSession(new AccountInfo(account.getUsername(), account.getCoins()));
                f = true;
            }
        }
        if(!f){
            throw new UserNotFoundException("Utente non trovato");
        }

    }


    public void registerUser(RegisterUserBean registerUserBean) throws UserAlreadyExistsException{
        if (accountDao.getAccountByUsername(registerUserBean.getUsername()) != null){
           throw new UserAlreadyExistsException("L'utente è già presente nel sistema");
        }
        Account account = new Account(registerUserBean.getUsername(),registerUserBean.getPassword(), registerUserBean.getAccountType());
        SessionManager.getInstance().createSession(new Session(account));
        AccountInfoSessionManager.getInstance().createAccountInfoSession(new AccountInfo(account.getUsername(), account.getCoins()));
        accountDao.addAccount(account);
        if(registerUserBean.getAccountType() == AccountType.COSTUMER){
            Costumer costumer = new Costumer(registerUserBean.getName(), registerUserBean.getSurname(), account);
            costumerDao.addCostumer(costumer);
        }else{
            Organizer organizer = new Organizer(registerUserBean.getName(), registerUserBean.getSurname(), account);
            organizerDao.addOrganizer(organizer);
        }

    }
}
