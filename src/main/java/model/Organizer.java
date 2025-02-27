package model;

import login.Account;
import login.User;

import java.util.ArrayList;
import java.util.List;

public class Organizer extends User {
    public List<Event> createdEventList;
    public List<Auction> createdAuctionList;

    public Organizer( String name, String surname, Account account){
        super(name,surname,account);
    }

    public Organizer(String name, String surname, Account account, List<Event>createdEventList, List<Auction>createdAuctionList){
        super(name, surname, account);
        this.createdEventList = createdEventList;
        this.createdAuctionList = createdAuctionList;
    }

    public void addEvent(Event event){
        this.createdEventList.add(event);
    }

    public List<Event> getAllEvents(){
        return this.createdEventList;
    }

    public void addAuction(Auction auction){
        this.createdAuctionList.add(auction) ;
    }

    public void setCreatedAuctionList(List<Auction> createdAuctionList){
        this.createdAuctionList = createdAuctionList;
    }

    public List<Auction> getAllAuctions(){
        return this.createdAuctionList;
    }



}
