package model;

public class User {
    private String name;
    private String password;
    private Integer coins;

    public User(String name, String password){
        this.name = name;
        this.password = password;
        this.coins = 20;
    }

    public String getName(){
        return this.name;
    }

    public Integer getCoins(){
        return this.coins;
    }

    public void setCoins(Integer coins){
        this.coins = coins;
    }

    //maybe here you can change a little bit
    public void updateCoins(){
        this.coins = coins+1;
    }
}
