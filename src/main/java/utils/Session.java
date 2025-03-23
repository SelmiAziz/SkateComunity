package utils;

import login.Role;

import java.time.Instant;

public class Session {
    private final String usernameUserLogged;
    private final Role role;
    private final Instant loggTime;

    public Session(String usernameUserLogged, Role role){
        this.usernameUserLogged = usernameUserLogged;
        this.role = role;
        this.loggTime = Instant.now();

    }

    public  String getUsername(){
        return this.usernameUserLogged;
    }


    public Role getRole(){
        return this.role;
    }





    public Instant getLoggTime(){
        return this.loggTime;
    }
}
