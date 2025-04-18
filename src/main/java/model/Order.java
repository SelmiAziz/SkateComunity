package model;

import model.decorator.Skateboard;

import java.util.List;

public class Order {
    private Customer customer;
    private DeliveryDestination deliveryDestination;
    private List<ProgressNote> progressNoteList;
    private Skateboard skateboard;


    public Order(Customer customer, DeliveryDestination deliveryDestination, Skateboard skateboard){
        this.deliveryDestination = deliveryDestination;
        this.skateboard = skateboard;
        this.customer = customer;
    }

    public int totalCost(){
        return skateboard.price() + deliveryDestination.deliveryCost();
    }

    public void writeNote(ProgressNote progressNote){
        this.progressNoteList.add(progressNote);
    }

    public List<ProgressNote> progressDetails(){
        return progressNoteList;
    }

    public Customer getCustomer() {
        return customer;
    }

    public DeliveryDestination getDeliveryDestination() {
        return deliveryDestination;
    }

    public Skateboard getSkateboard() {
        return skateboard;
    }
}
