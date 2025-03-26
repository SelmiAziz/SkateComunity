package model;

import login.Role;
import login.User;


import java.util.ArrayList;
import java.util.List;

public class Organizer extends User {
    public List<Event> eventCreatedList;
    public List<SkateboardCommission> skateboardCommissionInChargeList;

    public Organizer(String username, String password,String dateOfBirth){
        super(username,password,dateOfBirth);
        this.role = Role.ORGANIZER;
        this.eventCreatedList = new ArrayList<>();
        this.skateboardCommissionInChargeList = new ArrayList<>();
    }



    public void addEvent(Event event){
        this.eventCreatedList.add(event);
    }

    public List<Event> getEventCreatedList(){
        return this.eventCreatedList;
    }

    public void addSkateboardCommissionInCharge(SkateboardCommission skateboardCommission){
        this.skateboardCommissionInChargeList.add(skateboardCommission);
    }

    public List<SkateboardCommission> getSkateboardCommissionInChargeList(){
        return skateboardCommissionInChargeList;
    }

}
