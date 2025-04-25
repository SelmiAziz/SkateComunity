package model;

import model.decorator.Board;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Order {
    String id;
    private Customer customer;
    private DeliveryDestination deliveryDestination;
    private List<ProgressNote> progressNoteList = new ArrayList<>();
    private Board board;
    private OrderStatus orderStatus;


    public Order(Customer customer, DeliveryDestination deliveryDestination, DeliveryPreferences deliveryPreferences, Board board){
        this.deliveryDestination = deliveryDestination;
        this.board = board;
        this.customer = customer;
        this.progressNoteList.add(new ProgressNote("Chronology Starts here!!"));
        this.orderStatus = OrderStatus.REQUESTED;
        this.id =  UUID.randomUUID().toString();

        //CustomOrder compose deliveryPreferences


    }

    public LocalDate creationDate() {
        return progressNoteList.get(progressNoteList.size() - 1).getDate();
    }

    public LocalDate deliveryDate() {
        if(orderStatus == OrderStatus.COMPLETED){
            return progressNoteList.get(0).getDate();
        }
        return null;
    }

    public int totalCost(){
        return board.price() ;
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
        return board;
    }


}
