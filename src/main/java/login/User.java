package login;

public class User {
    private String username;
    private String password;
    private Profile profile;

    public User( String username, String password){

        this.username = username;
        this.password = password;
    }

    public Profile getProfile(){
        return this.profile;
    }

    public void setProfile(Profile profile){
        this.profile = profile;
    }


    public String getUsername(){return this.username;}

    public void setUsername(String username){
        this.username = username;
    }

    public String getPassword(){
        return this.password;
    }

    public void setPassword(String password){
        this.password = password;
    }








}


