package utils;

import login.ProfileType;

import java.time.Instant;

public class Session {
    private final String username;
    private final String nameProfile;
    private final ProfileType profileType;
    private final Instant loggTime;

    public Session(String username, String nameProfile, ProfileType profileType){
        this.username = username;
        this.nameProfile = nameProfile;
        this.profileType = profileType;
        this.loggTime = Instant.now();

    }

    public  String getUsername(){
        return this.username;
    }

    public String getNameProfile(){
        return this.nameProfile;
    }

    public ProfileType getProfileType(){
        return this.profileType;
    }





    public Instant getLoggTime(){
        return this.loggTime;
    }
}
