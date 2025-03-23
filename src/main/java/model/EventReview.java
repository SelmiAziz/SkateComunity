package model;

public class EventReview {
    int rating;
    String description;
    Customer customerReviewer;

    public EventReview(String description, int rating, Customer customerReviewer){
        this.description = description;
        this.rating = rating;
        this.customerReviewer = customerReviewer;
    }

    String getDescription(){
        return this.description;
    }

    int getRating(){
        return this.rating;
    }

    Customer getCustomerReviewer(){
        return this.customerReviewer;
    }

    public void setCustomerReviewer(Customer customerReviewer){
        this.customerReviewer = customerReviewer;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public void setRating(int rating){
        this.rating = rating;
    }

}
