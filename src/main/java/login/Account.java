package login;

public class Account {
    private String username;
    private String password;
    private AccountType accountType;
    private int coinsNumber;

    public Account(String username, String password, AccountType accountType){
        this.username = username;
        this.password = password;
        this.accountType = accountType;
        this.coinsNumber = 150;
    }

    public Account(String username, String password, AccountType accountType, int coinsNumber){
        this.username = username;
        this.password = password;
        this.accountType = accountType;
        this.coinsNumber = coinsNumber;
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

    public int getCoins(){
        return this.coinsNumber;
    }

    public void decrementCoins(int decrementCoins){
        this.coinsNumber -= decrementCoins;
    }

    public void incrementCoins(int incrementCoins){
        this.coinsNumber += incrementCoins;
    }
}
