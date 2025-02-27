package model;

public class AuctionProposal {
    private Costumer custumer;
    private String state;

    public AuctionProposal(Costumer custumer, String state){
        this.custumer = custumer;
        this.state = state;
    }

    public AuctionProposal(Costumer custumer){
        this.custumer= custumer;
    }

    public void setState(String state){

        this.state = state;
    }

    public String getState(){
        return this.state;
    }
}
