package utils;

import login.Account;

import java.time.Instant;

public class Session {
    private Account account;
    private Instant loggTime;

    public Session(Account account){
        this.account= account;
        this.loggTime = Instant.now();
    }

    public Account getAccount(){
        return this.account;
    }

    public Instant getLoggTime(){
        return this.loggTime;
    }
}
