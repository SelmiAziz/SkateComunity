package model;

public class Wallet {
    int walletId;
    int balance;

    public Wallet(int walletId, int balance){
        this.walletId = walletId;
        this.balance = balance;
    }


    public Wallet(){
        this.balance = 100;
    }



    public void setBalance(int balance){
        this.balance = balance;
    }

    public int getBalance(){
        return this.balance;
    }

    public void payCoins(int coinsPayed){
        this.balance = balance -coinsPayed;
    }


    public void depositCoins(int coinsGained){
        this.balance += coinsGained;
    }

    public void setWalletId(int walletId){
        this.walletId = walletId;
    }

    public int getWalletId(){
        return this.walletId;
    }

    public String toCsvString(){ return String.format("%d,%d", walletId, balance);}
}
