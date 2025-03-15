package model;

public class Competition {
    private Product product;
    private Organizer organizer;

    public Competition(Product product, Organizer organizer){
        this.product  = product;
        this.organizer = organizer;
    }

    public Organizer getOrganizer(){
        return this.organizer;
    }

    public Product getProduct(){
        return this.product;
    }





}
