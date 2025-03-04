package utils;

public class AccountInfoSession {
    private static AccountInfoSession instance;
    private AccountInfo accountInfo;

    public AccountInfoSession(){}

    public static AccountInfoSession getInstance(){
        if(instance == null){
            instance = new AccountInfoSession();
        }
        return instance;
    }

    public void createAccountInfoSession(AccountInfo accountInfo){
        this.accountInfo = accountInfo;
    }

    public AccountInfo getAccountInfo(){
        return this.accountInfo;
    }
}
