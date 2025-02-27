package model;

import java.util.ArrayList;
import java.util.List;

public class Auction {
    private Product product;
    private Organizer organizer;
    private List<AuctionProposal> proposals;

    public Auction(Product product, Organizer organizer){
        this.product  = product;
        this.organizer = organizer;
        this.proposals = new ArrayList<>();
    }

    public Organizer getOrganizer(){
        return this.organizer;
    }

    public Product getProduct(){
        return this.product;
    }

    public void addProposal(AuctionProposal proposal){
        proposals.add(proposal);
    }

    public  List<AuctionProposal> getProposals(){
        return this.proposals;
    }



}
