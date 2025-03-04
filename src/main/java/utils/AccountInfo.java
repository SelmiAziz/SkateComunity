package utils;

public class AccountInfo {
    private String username;
    private int coinsNumber;

    public AccountInfo(String username, int coinsNumber){
        this.username = username;
        this.coinsNumber = coinsNumber;
    }

    public void updateCoinsNumber(int coinsNumber){
        this.coinsNumber = coinsNumber;
    }

    public void decrementCoins(int coinsDecrement){
        this.coinsNumber -= coinsDecrement;
    }

    public void incrementCoinst(int coinsIncrement){
        this.coinsNumber += coinsIncrement;
    }

    public String getUsername(){
        return this.username;
    }

    public int getCoinsNumber(){
        return this.coinsNumber;
    }
}
