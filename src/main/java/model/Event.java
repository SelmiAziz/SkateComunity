package model;

import java.util.ArrayList;
import java.util.List;

public class Event {
    private final String name;
    private final String description;
    private final String date;
    private final int participationFee;
    private final String location;
    private final Integer maxRegistrations;
    private List<EventRegistration> requestList;
    private  Organizer organizer;

    public Event(String name, String description, String date, String location, int participationFee, int maxRegistrations){
        this.name = name;
        this.description = description;
        this.date = date;
        this.participationFee= participationFee;
        this.location = location;
        this.maxRegistrations = maxRegistrations;
        this.requestList = new ArrayList<>();
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

    public String getLocation(){
        return this.location;
    }

    public Integer getParticipationFee(){
        return this.participationFee;
    }

    public Integer getMaxRegistrations(){
        return this.maxRegistrations;
    }

    public Integer getCurrentRegistrations(){
        return requestList.toArray().length;
    }



    public void addEventRegistration(EventRegistration request){
        this.requestList.add(request);
    }

    public  List<EventRegistration> getEventRegistrations(){
        return this.requestList;
    }

}
