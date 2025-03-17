package login;

public abstract class Profile {
    private final String name;
    protected int coins;

    public Profile(String name, int coins){
        this.name = name;
        this.coins = coins;
    }


    public void payCoins(int coins){
        this.coins -= coins;
    }

    public void gainCoins(int coins){
        this.coins += coins;
    }

    public int getCoins(){
        return this.coins;
    }

    public String getName(){
        return this.name;
    }



    public abstract ProfileType getProfileType();

}
