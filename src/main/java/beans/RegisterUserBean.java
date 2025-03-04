package beans;

import login.AccountType;

public class RegisterUserBean {
    private String name;
    private String surname;
    private String username;
    private String password;
    private AccountType accountType;

    public RegisterUserBean(String name, String surname, String username, String password, AccountType accountType){
        this.name = name;
        this.surname = surname;
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

    public String getName(){
        return this.name;
    }

    public String getSurname(){
        return this.surname;
    }
}
