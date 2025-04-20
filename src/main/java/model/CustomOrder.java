package model;

import model.decorator.Board;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CustomOrder {
    String id;
    private Customer customer;
    private DeliveryDestination deliveryDestination;
    private List<ProgressNote> progressNoteList = new ArrayList<>();
    private Board skateboard;
    private OrderStatus orderStatus;


    public CustomOrder(Customer customer, DeliveryDestination deliveryDestination, DeliveryPreferences deliveryPreferences, Board skateboard){
        this.deliveryDestination = deliveryDestination;
        this.skateboard = skateboard;
        this.customer = customer;
        this.progressNoteList.add(new ProgressNote());
        this.orderStatus = OrderStatus.REQUESTED;
        this.id =  UUID.randomUUID().toString();

        //CustomOrder compose deliveryPreferences


    }

    public String creationDate() {
        return progressNoteList.get(progressNoteList.size() - 1).getDate();
    }

    public String deliveryDate() {
        if(orderStatus == OrderStatus.COMPLETED){
            return progressNoteList.get(0).getDate();
        }
        return null;
    }

    public int totalCost(){
        return skateboard.price() ;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public String getId() {
        return id;
    }

    public List<ProgressNote> progressDetails(){
        return progressNoteList;
    }

    public void addProgressNoteOrder(ProgressNote progressNote) {
        this.progressNoteList.add(progressNote);
    }



    public Customer getCustomer() {
        return customer;
    }

    public DeliveryDestination getDeliveryDestination() {
        return deliveryDestination;
    }

    public Board getBoard() {
        return skateboard;
    }


}
