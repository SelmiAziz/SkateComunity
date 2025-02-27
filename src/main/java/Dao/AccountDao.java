package Dao;

import login.Account;
import login.AccountType;

import java.util.List;

public interface AccountDao {
    public Account getAccountByUsername(String username);
    public void addAccount(Account account);
   public List<Account> getAllAccounts();
   public Account checkAccount(String username, String password, AccountType accountType);
}
