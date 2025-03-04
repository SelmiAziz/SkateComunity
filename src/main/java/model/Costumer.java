package model;

import login.Account;
import login.User;

import java.util.List;

public class Costumer extends User {
    private List<AuctionProposal> auctionProposalList;

    public Costumer(String name, String surname, Account account){
      super(name,surname,account);
    }

    public Costumer(String name, String surname, Account account, List<AuctionProposal> auctionProposalList){
        super(name,surname,account);
        this.auctionProposalList = auctionProposalList;
    }

    public List<AuctionProposal> getAllAuctionProposals() {
        return auctionProposalList;
    }

    public void addAuctionProposal( AuctionProposal auctionProposal){
        this.auctionProposalList.add(auctionProposal);
    }

    public void setAuctionProposalList(List<AuctionProposal> auctionProposalList){
        this.auctionProposalList = auctionProposalList;
    }

    public void payCoins(int coinsPayed){
        super.getAccount().decrementCoins(coinsPayed);
    }
}
