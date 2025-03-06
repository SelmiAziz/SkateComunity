package model;

import java.util.ArrayList;
import java.util.List;

public class Event {
    private final String name;
    private final String description;
    private final String date;
    private final int coins;
    private final String country;
    private final Integer maxRegistrations;
    private Integer currentRegistrations;
    private List<EventParticipation> eventParticipationList;
    private  Organizer organizer;

    public Event(String name, String description,String date, String country, int coins, Integer maxRegistrations){
        this.name = name;
        this.description = description;
        this.date = date;
        this.coins = coins;
        this.country = country;
        this.currentRegistrations = 0;
        this.maxRegistrations = maxRegistrations;
        this.eventParticipationList = new ArrayList<>();
    }

    public void setOrganizer(Organizer organizer){
        this.organizer = organizer;
    }
    public Organizer getOrganizer(){
        return this.organizer;
    }

    public  String getName(){
        return this.name;
    }

    public String getDescription(){
        return this.description;
    }

    public String getDate(){
        return this.date;
    }

    public String getCountry(){
        return this.country;
    }

    public Integer getCoins(){
        return this.coins;
    }



    public Integer getMaxRegistrations(){
        return this.maxRegistrations;
    }

    public Integer getCurrentRegistrations(){
        return this.currentRegistrations;
    }

    public List<EventParticipation>  getParticipants(){
        return this.eventParticipationList;
    }


    public void addEventParticipation(EventParticipation eventParticipation){
        this.eventParticipationList.add(eventParticipation);
        this.currentRegistrations +=1;
    }

    public  List<EventParticipation> getEventParticipations(){
        return this.eventParticipationList;
    }

}
