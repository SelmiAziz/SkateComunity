package beans;

import model.Event;

public class EventBean {
    private String name;
    private String description;
    private String country;
    private String date;
    private int coins;
    private int availableRegistrations;
    private int maxRegistrations;
    private int currentRegistrations;

    public EventBean(String name, String description, String date, String country, int coins,int maxRegistrations){
        this.name = name;
        this.description = description;
        this.date = date;
        this.country = country;
        this.coins = coins;
        this.maxRegistrations = maxRegistrations;
    }

    public EventBean(String name, String description, String date, String country, int coins,int currentRegistrations,int maxRegistrations){
        this.name = name;
        this.description = description;
        this.date = date;
        this.country = country;
        this.coins = coins;
        this.currentRegistrations = currentRegistrations;
        this.maxRegistrations = maxRegistrations;
    }


    public EventBean(String name, String description, String date){
        this.name = name;
        this.description = description;
        this.date = date;
    }

    public EventBean(String country, String date){
        this.country = country;
        this.date = date;
    }

    public EventBean(String name, String description, int coins, int availableRegistrations){
        this.name = name;
        this.description = description;
        this.coins = coins;
        this.availableRegistrations = availableRegistrations;
    }


    public String getName(){
        return this.name;
    }

    public String getDescription(){
        return this.description;
    }

    public String getCountry(){
        return this.country;
    }

    public String getDate(){
        return this.date;
    }

    public int getCoins(){
        return this.coins;
    }

    public int getCurrentRegistrations(){
        return this.currentRegistrations;
    }

    public int getAvailableRegistrations(){
        return this.availableRegistrations;
    }

    public int getMaxRegistrations(){
        return this.maxRegistrations;
    }

}
