package model;

import dao.patternObserver.Observer;
import dao.patternObserver.Subject;
import login.Profile;
import login.ProfileType;

import java.util.ArrayList;
import java.util.List;

public class Costumer extends Profile implements Subject {
    private List<Competition> negotiationList;
    private List<EventRegistration> eventParticipationList ;
    private List<Observer> observerList = new ArrayList<>();


    public Costumer(String name){
        super(name,100);
        this.negotiationList = new ArrayList<>();
        this.eventParticipationList = new ArrayList<>();
    }

    public Costumer(String name, int coins){
        super(name,coins);
        this.negotiationList = new ArrayList<>();
        this.eventParticipationList = new ArrayList<>();
    }

    @Override
    public void attach(Observer observer) {
        observerList.add(observer);
    }

    @Override
    public void detach(Observer observer){

    }

    @Override
    public void notifyObservers(){
        for(Observer observer:observerList){
            observer.update();
        }
    }

    @Override
    public void payCoins(int coins){
        super.coins -= coins;
        notifyObservers();
    }

    @Override
    public ProfileType getProfileType() {
        return ProfileType.COSTUMER;
    }

    public List<Competition> getAllNegotations() {
        return negotiationList ;
    }

    public void addNegotiation( Competition negotiation){
        this.negotiationList.add(negotiation);
    }



}
