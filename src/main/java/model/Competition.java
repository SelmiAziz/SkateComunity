package model;

import java.util.ArrayList;
import java.util.List;

public class Competition {
    private  String competitionName;
    private  String description;
    private  String date;
    private  int participationFee;
    private String location;
    private Integer maxRegistrations;
    private List<Registration> requestList;
    private  Organizer organizer;

    public Competition(String competitionName, String description, String date, String location, int participationFee, int maxRegistrations){
        this.competitionName = competitionName;
        this.description = description;
        this.date = date;
        this.participationFee= participationFee;
        this.location = location;
        this.maxRegistrations = maxRegistrations;
        this.requestList = new ArrayList<>();
    }


    public Competition(){}


    public void setOrganizer(Organizer organizer){
        this.organizer = organizer;
    }
    public Organizer getOrganizer(){
        return this.organizer;
    }

    public  String getName(){
        return this.competitionName;
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

    public Integer getRegistrationsNumber(){
        return requestList.toArray().length;
    }



    public void addCompetitionRegistration(Registration competitionRegistration){
        this.requestList.add(competitionRegistration);
    }

    public  List<Registration> getCompetitionRegistrations(){
        return this.requestList;
    }

}
