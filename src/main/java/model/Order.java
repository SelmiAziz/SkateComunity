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
    private DeliveryPreferences deliveryPreferences;
    private List<ProgressNote> progressNoteList = new ArrayList<>();
    private Board board;
    private OrderStatus orderStatus;


    public Order( DeliveryDestination deliveryDestination, DeliveryPreferences deliveryPreferences, Board board){
        this.deliveryDestination = deliveryDestination;
        this.board = board;
        this.customer = customer;
        this.progressNoteList.add(new ProgressNote("Chronology Starts here!!"));
        this.orderStatus = OrderStatus.REQUESTED;
        this.id =  UUID.randomUUID().toString();

        //order compose deliveryPreferences
       this.deliveryPreferences = new DeliveryPreferences(deliveryPreferences.getPreferredTimeSlot(), deliveryPreferences.getComment());

    }


    public LocalDate creationDate() {
        return progressNoteList.get(0).getDate();
    }

    public LocalDate deliveryDate() {
        if(orderStatus == OrderStatus.COMPLETED){
            return progressNoteList.get(progressNoteList.size() - 1).getDate();
        }
        return null;
    }


    public List<ProgressNote> progressNoteChronology(){
        return progressNoteList;
    }

    public void addProgressNote(ProgressNote progressNote) {
        this.progressNoteList.add(progressNote);
    }

    public String commentOrderPreferences(){
        return deliveryPreferences.getComment();
    }

    public String timeSlotOrderPreferences(){
        return deliveryPreferences.getPreferredTimeSlot();
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

    public void setId(String id){
        this.id = id;
    }


    public void setCustomer(Customer customer){
        this.customer = customer;
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
