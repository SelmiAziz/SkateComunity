package login;

public class Account {
    private String username;
    private String password;
    private AccountType accountType;
    private Integer coins;

    public Account(String username, String password, AccountType accountType){
        this.username = username;
        this.password = password;
        this.accountType = accountType;
        this.coins = 0;
    }

    public Account(String username, String password, AccountType accountType, Integer coins){
        this.username = username;
        this.password = password;
        this.accountType = accountType;
        this.coins = coins;
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
}
