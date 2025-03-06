package model;

import java.util.ArrayList;
import java.util.List;

public class Negotiation {
    private Product product;
    private Organizer organizer;

    public Negotiation(Product product, Organizer organizer){
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
