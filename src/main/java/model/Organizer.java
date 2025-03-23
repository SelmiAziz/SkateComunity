package model;

import login.Role;
import login.User;


import java.util.ArrayList;
import java.util.List;

public class Organizer extends User {
    public List<Event> eventCreatedList = new ArrayList<>();
    public List<Competition> competitionCreatedList = new ArrayList<>();

    public Organizer(String username, String password,String dateOfBirth){
        super(username,password,dateOfBirth);
        this.role = Role.ORGANIZER;
    }



    public void addEvent(Event event){
        this.eventCreatedList.add(event);
    }

    public List<Event> getEventCreatedList(){
        return this.eventCreatedList;
    }


    public void addCompetition(Competition competition){this.competitionCreatedList.add(competition);}

    public List<Competition> getCompetitionCreatedList(){return this.competitionCreatedList;}


}
