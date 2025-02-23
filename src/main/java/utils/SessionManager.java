package utils;

import model.User;

public class SessionManager {
    private static SessionManager instance;
    private User loggedUser;

    private SessionManager(){}

    // I dind't make it synchornized
    public synchronized static SessionManager getInstance(){
        if(instance == null){
            instance = new SessionManager();
        }
        return instance;
    }
    public void login(User user){
        this.loggedUser = user;
    }

    public void logout(){
        this.loggedUser = null;
    }

    public boolean isLogged(){
        return this.loggedUser != null;
    }

    public User getLoggedUser(){
        return this.loggedUser;
    }
}
