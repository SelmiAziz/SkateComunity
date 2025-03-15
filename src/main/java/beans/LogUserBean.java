package beans;

import login.ProfileType;

public class LogUserBean {
    private String username;
    private String password;
    private ProfileType profileType;

    public LogUserBean(String username, String password){
        this.username = username;
        this.password = password;
    }

    public LogUserBean(ProfileType profileType){
        this.profileType = profileType;
    }

    public ProfileType getProfileType() {
        return profileType;
    }

    public String getUsername(){
        return this.username;
    }

    public String getPassword(){
        return this.password;
    }


    public void setUsername(String username){
        this.username = username;
    }

    public void setPassword(String password){
        this.password = password;
    }




}
