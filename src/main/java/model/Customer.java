package model;

import login.Role;
import login.User;
import model.decorator.Board;
import utils.SkaterLevel;

import java.util.ArrayList;
import java.util.List;

public class Customer extends User {
    private List<Registration> competitionRegistrationList ;// a value of default
    private SkaterLevel skaterLevel;
    private List<Order> ordersSubmittedList;
    private List<Order> ordersAcquiredList;
    private List<Board> boardDesignedList;
    private Wallet wallet;

   public Customer(String username){
       super(username);
   }

    public Customer(String username, String password, String dateOfBirth, SkaterLevel skaterLevel, Wallet wallet){
        super(username,password,dateOfBirth);
        this.role = Role.COSTUMER;
        this.wallet = wallet;
        this.competitionRegistrationList = new ArrayList<>();
        this.ordersSubmittedList = new ArrayList<>();
        this.ordersAcquiredList = new ArrayList<>();
        this.boardDesignedList = new ArrayList<>();
        this.skaterLevel = skaterLevel;
    }






    public void addCompetitionRegistration(Registration competitionRegistration){this.competitionRegistrationList.add(competitionRegistration);}
    public int totalRegistrationNumber(){
       return competitionRegistrationList.size();
    }


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


    public List<Registration> getCompetitionRegistrationList() {
        return competitionRegistrationList;
    }

    public void addSubmittedOrder(Order order){
        this.ordersSubmittedList.add(order);
    }

    public List<Order> customOrdersSubmitted(){
        return ordersSubmittedList;
    }

    public void addAcquiredOrder(Order order){
        this.ordersAcquiredList.add(order);
    }

    public List<Order> customOrdersAcquired(){
        return this.ordersAcquiredList;
    }

    public void addDesignBoard(Board board){
        this.boardDesignedList.add(board);
    }

    public List<Board> customizedBoards(){
        return this.boardDesignedList;
    }




}
