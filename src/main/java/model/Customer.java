package model;

import login.Role;
import login.User;
import utils.SkaterLevel;

import java.util.ArrayList;
import java.util.List;

public class Customer extends User {
    private List<EventRegistration> eventRegistrationList = new ArrayList<>() ;
    private List<Application> applicationList = new ArrayList<>();
    private List<Competition> competitionList = new ArrayList<>();
    private int coins = 100; // a value of default
    private SkaterLevel skaterLevel;


    public Customer(String username, String password, String dateOfBirth, SkaterLevel skaterLevel){
        super(username,password,dateOfBirth);
        this.role = Role.COSTUMER;
        this.skaterLevel = skaterLevel;
    }

    public Customer(String username, String password, String dateOfBirth, SkaterLevel skaterLevel, int coins){
        super(username, password,dateOfBirth);
        this.role = Role.COSTUMER;
        this.skaterLevel = skaterLevel;
        this.coins = coins;
    }

    public void addEventRegistration(EventRegistration eventRegistration){this.eventRegistrationList.add(eventRegistration);}
    public void addApplicationRegistration(Application application){this.applicationList.add(application);}
    public void addCompetition(Competition competition){this.competitionList.add(competition);}

    public void setCoins(int coins){
        this.coins = coins;
    }

    public int getCoins(){
        return this.coins;
    }

    public void pay(int coinsPayed){
        this.coins -= coinsPayed;
    }

    public void setSkaterLevel(SkaterLevel skaterLevel) {
        this.skaterLevel = skaterLevel;
    }

    public SkaterLevel getSkaterLevel(){
        return this.skaterLevel;
    }

    public List<EventRegistration> getEventParticipationList(){return this.eventRegistrationList;}
    public List<Application> getApplicationList(){return this.applicationList;}
    public List<Competition> getCompetitionList(){return this.competitionList;}



}
