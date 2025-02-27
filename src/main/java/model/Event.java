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
    private List<Costumer> participants;
    private String manager;

    public Event(String name, String description,String date, Integer coins, String country, Integer maxRegistrations, String manager ){
        this.name = name;
        this.description = description;
        this.date = date;
        this.coins = coins;
        this.country = country;
        this.currentRegistrations = 0;
        this.maxRegistrations = maxRegistrations;
        this.participants = new ArrayList<>();
        this.manager = manager;
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

    public void addRegistration(Costumer participant){
        this.currentRegistrations += 1;
        this.participants.add(participant);
    }

    public Integer getMaxRegistrations(){
        return this.maxRegistrations;
    }

    public Integer getCurrentRegistrations(){
        return this.currentRegistrations;
    }


}
