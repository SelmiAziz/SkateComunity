package model;

import login.Role;
import login.User;
import utils.SkaterLevel;

import java.util.ArrayList;
import java.util.List;

public class Customer extends User {
    private List<CompetitionRegistration> competitionRegistrationList ;// a value of default
    private SkaterLevel skaterLevel;
    private List<SkateboardCommission> skateboardCommissionsSubmittedList;
    private List<SkateboardCommission> skateboardCommissionsApproved;
    private Wallet wallet;

    public Customer(String username, String password, String dateOfBirth, SkaterLevel skaterLevel, Wallet wallet){
        super(username,password,dateOfBirth);
        this.role = Role.COSTUMER;
        this.wallet = wallet;
        this.competitionRegistrationList = new ArrayList<>();
        this.skaterLevel = skaterLevel;
    }






    public void addCompetitionRegistration(CompetitionRegistration competitionRegistration){this.competitionRegistrationList.add(competitionRegistration);}



    public void setSkaterLevel(SkaterLevel skaterLevel) {
        this.skaterLevel = skaterLevel;
    }

    public SkaterLevel getSkaterLevel(){
        return this.skaterLevel;
    }


    public void setWallet(Wallet wallet){
        this.wallet = wallet;
    }

    public Wallet getWallet(){
        return this.wallet;
    }


    public List<CompetitionRegistration> getCompetitionRegistrationList() {
        return competitionRegistrationList;
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
