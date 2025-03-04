package beans;

import login.AccountType;

public class LogUserBean {
    private String username;
    private String password;
    private AccountType accountType;

    public LogUserBean(String username, String password, AccountType accountType){
        this.username = username;
        this.password = password;
        this.accountType = accountType;
    }


    public String getUsername(){
        return this.username;
    }

    public String getPassword(){
        return this.password;
    }

    public AccountType getAccountType(){
        return this.accountType;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public void setAccountType(AccountType accountType){
        this.accountType = accountType;
    }


}
