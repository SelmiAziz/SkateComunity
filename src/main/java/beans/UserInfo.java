package beans;

public class UserInfo {
    private  String username;
    private  int coins;

    public UserInfo(String username, int coins){
        this.username = username;
        this.coins = coins;
    }

    public int getCoins(){
        return this.coins;
    }

    public String getUsername(){
        return this.username;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public void setCoins(int coins){
        this.coins = coins;
    }
}
