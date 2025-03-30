package model;

import login.Role;
import login.User;


import java.util.ArrayList;
import java.util.List;

public class Organizer extends User {
    public List<Competition> competitionCreatedList;
    public List<SkateboardCommission> skateboardCommissionInChargeList;

    public Organizer(String username, String password,String dateOfBirth){
        super(username,password,dateOfBirth);
        this.role = Role.ORGANIZER;
        this.competitionCreatedList = new ArrayList<>();
        this.skateboardCommissionInChargeList = new ArrayList<>();
    }



    public void addCompetition(Competition competition){
        this.competitionCreatedList.add(competition);
    }

    public List<Competition> getCompetitionCreatedList(){
        return this.competitionCreatedList;
    }

    public void addSkateboardCommissionInCharge(SkateboardCommission skateboardCommission){
        this.skateboardCommissionInChargeList.add(skateboardCommission);
    }

    public List<SkateboardCommission> getSkateboardCommissionInChargeList(){
        return skateboardCommissionInChargeList;
    }

}
