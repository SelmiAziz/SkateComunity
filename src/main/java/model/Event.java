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
    private List<EventRegistration> eventRegistrationList;
    private  Organizer organizer;

    public Event(String name, String description,String date, String country, int coins, int maxRegistrations){
        this.name = name;
        this.description = description;
        this.date = date;
        this.coins = coins;
        this.country = country;
        this.maxRegistrations = maxRegistrations;
        this.eventRegistrationList = new ArrayList<>();
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
        return eventRegistrationList.toArray().length;
    }



    public void addEventRegistration(EventRegistration eventParticipation){
        this.eventRegistrationList.add(eventParticipation);
    }

    public  List<EventRegistration> getEventRegistrations(){
        return this.eventRegistrationList;
    }

}
