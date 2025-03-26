package model;

import login.Role;
import login.User;
import utils.SkaterLevel;

import java.util.ArrayList;
import java.util.List;

public class Customer extends User {
    private List<EventRegistration> eventRegistrationList ;// a value of default
    private SkaterLevel skaterLevel;
    private List<SkateboardCommission> skateboardCommissionsSubmittedList;
    private List<SkateboardCommission> skateboardCommissionsApproved;


    public Customer(String username, String password, String dateOfBirth, SkaterLevel skaterLevel){
        super(username,password,dateOfBirth);
        this.role = Role.COSTUMER;
        this.eventRegistrationList = new ArrayList<>();
        this.skaterLevel = skaterLevel;
    }


    public void addEventRegistration(EventRegistration eventRegistration){this.eventRegistrationList.add(eventRegistration);}



    public void setSkaterLevel(SkaterLevel skaterLevel) {
        this.skaterLevel = skaterLevel;
    }

    public SkaterLevel getSkaterLevel(){
        return this.skaterLevel;
    }

    public List<EventRegistration> getEventRegistrationList() {
        return eventRegistrationList;
    }

    public void addSkateboardCommissionApproved(SkateboardCommission skateboardCommission){
        this.skateboardCommissionsApproved.add(skateboardCommission);
    }

    public void addSkateboardCommissionSubmitted(SkateboardCommission skateboardCommission){
        this.skateboardCommissionsSubmittedList.add(skateboardCommission);
    }

    public List<SkateboardCommission> getSkateboardCommissionsApproved() {
        return skateboardCommissionsApproved;
    }


    public List<SkateboardCommission> getSkateboardCommissionsSubmittedList() {
        return skateboardCommissionsSubmittedList;
    }
}
