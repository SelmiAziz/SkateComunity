package model;

import login.Profile;
import login.ProfileType;

import java.util.ArrayList;
import java.util.List;

public class Costumer extends Profile {
    private List<Competition> negotiationList;
    private List<EventRegistration> eventParticipationList ;



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
