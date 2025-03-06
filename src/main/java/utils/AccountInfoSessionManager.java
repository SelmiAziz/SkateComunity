package utils;

public class AccountInfoSessionManager {
    private static AccountInfoSessionManager instance;
    private AccountInfo accountInfo;

    public AccountInfoSessionManager(){}

    public static AccountInfoSessionManager getInstance(){
        if(instance == null){
            instance = new AccountInfoSessionManager();
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
