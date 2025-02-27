package beans;

import login.AccountType;

public class RegisterAccountBean {
    private String username;
    private String password;
    private AccountType accountType;

    public RegisterAccountBean(String username, String password){
        this.username = username;
        this.password = password;
        this.accountType = accountType;
    }

    public String getUsername(){
        return this.username;
    }

    public AccountType getAccountType(){
        return this.accountType;
    }

    public String getPassword(){
        return this.password;
    }
}
