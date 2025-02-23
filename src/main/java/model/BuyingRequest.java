package model;

public class BuyingRequest {
    private Product product;
    private String seller;
    private String buyer;
    private String state;

    public BuyingRequest(Product product, String seller){
        this.product  = product;
        this.seller = seller;
    }

    public String getSeller(){
        return this.seller;
    }

    public Product getProduct(){
        return this.product;
    }

    public void setBuyer(String buyer){
        this.buyer = buyer;
    }

    public String getBuyer(){
        return this.buyer;
    }

    public void setState(String state){
        this.state = state;
    }

    public String getState(){
        return this.state;
    }
}
