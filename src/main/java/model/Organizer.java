package model;

import login.Account;
import login.User;

import java.util.List;

public class Organizer extends User {
    public List<Event> createdEventList;
    public List<Negotiation> createdAuctionList;

    public Organizer( String name, String surname, Account account){
        super(name,surname,account);
    }

    public Organizer(String name, String surname, Account account, List<Event>createdEventList, List<Negotiation>createdAuctionList){
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

    public void addAuction(Negotiation auction){
        this.createdAuctionList.add(auction) ;
    }

    public void setCreatedAuctionList(List<Negotiation> createdAuctionList){
        this.createdAuctionList = createdAuctionList;
    }

    public List<Negotiation> getAllAuctions(){
        return this.createdAuctionList;
    }

    public void gainCoins(int coinsGained){
        super.getAccount().incrementCoins(coinsGained);
    }

}
