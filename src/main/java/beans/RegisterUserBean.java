package beans;

import login.ProfileType;

public class RegisterUserBean {
    private String username;
    private String password;
    private String profileName;
    private ProfileType profileType;

    public RegisterUserBean( String username, String password, String profileName, ProfileType profileType){
;
        this.username = username;
        this.password = password;
        this.profileName = profileName;
        this.profileType = profileType;

    }

    public String getUsername(){
        return this.username;
    }

    public ProfileType getProfileType(){
        return this.profileType;
    }

    public String getPassword(){
        return this.password;
    }

    public String getProfileName(){
        return this.profileName;
    }

}
