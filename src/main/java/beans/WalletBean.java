package beans;

public class WalletBean {
    int balance;

    public WalletBean(){};
    public WalletBean(int balance){
        this.balance = balance;
    }


    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }
}
