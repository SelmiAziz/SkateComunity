package Dao;

import login.Account;
import login.AccountType;

import java.util.ArrayList;
import java.util.List;

public class AccountDemoDao implements AccountDao{
    private static AccountDemoDao instance;
    private List<Account> accountList;

    public AccountDemoDao() {
        this.accountList = new ArrayList<>();

    }

    public synchronized static AccountDemoDao getInstance() {
        if (instance == null) {
            instance = new AccountDemoDao();
        }
        return instance;
    }


    public void addAccount(Account account){
        this.accountList.add(account);
    }

    public List<Account>getAllAccounts(){
        return this.accountList;
    }

    public Account getAccountByUsername(String username){
        for( Account account: this.accountList){
            if(account.getUsername().equals(username)) {
                return account;
            }
        }
        return null;
    }

    public Account checkAccount(String username, String password, AccountType accountType){
        for(Account account : accountList){
            if(account.getUsername() == username && account.getPassword() == password && account.getAccountType() == accountType){
                return account;
            }
        }
        return null;
    }

}
