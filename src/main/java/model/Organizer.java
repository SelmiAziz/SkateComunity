package model;

import login.Role;
import login.User;


import java.util.ArrayList;
import java.util.List;

public class Organizer extends User {
    private  List<Competition> competitionCreatedList;

    public Organizer(String username, String password,String dateOfBirth){
        super(username,password,dateOfBirth);
        this.role = Role.ORGANIZER;
        this.competitionCreatedList = new ArrayList<>();
    }



    public void addCompetition(Competition competition){
        this.competitionCreatedList.add(competition);
    }

    public List<Competition> getCompetitionCreatedList(){
        return this.competitionCreatedList;
    }


}
