package model;

import login.Profile;
import login.ProfileType;


import java.util.ArrayList;
import java.util.List;

public class Organizer extends Profile {
    public List<Event> createdEventList;
    public List<Competition> negotiationList;

    public Organizer(String name){
        super(name,0);
        this.negotiationList = new ArrayList<>();
        this.createdEventList = new ArrayList<>();
    }

    public Organizer(String name, int coins){
        super(name,coins);
        this.negotiationList = new ArrayList<>();
        this.createdEventList = new ArrayList<>();
    }

    @Override
    public ProfileType getProfileType() {
        return ProfileType.ORGANIZER;
    }

    public void addEvent(Event event){
        this.createdEventList.add(event);
    }

    public List<Event> getAllEvents(){
        return this.createdEventList;
    }

    public void addNegotiation(Competition negotiation){
        this.negotiationList.add(negotiation);
    }

    public void setNegotiationList(List<Competition> negotiationList){
        this.negotiationList = negotiationList;
    }

    public List<Competition> getAllAuctions(){
        return this.negotiationList;
    }


}
