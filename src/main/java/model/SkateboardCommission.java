package model;

import java.util.ArrayList;
import java.util.List;

public class SkateboardCommission {
    private int budget;
    List<CustomizedRequest> customizedRequestList;
    private float discount;
    private float commissionFee;
    private Customer customer;
    private Organizer organizer;
    private String dataImmissione;
    private String dataEntroTermine;

    private SkateboardBase skateboard;


    SkateboardCommission(int budget,Customer customer, List<CustomizedRequest> customizedRequestList){
        this.discount = 0;
        this.commissionFee = 0;
        this.organizer = null;

        this.budget = budget;
        this.customer = customer;
        //make sure to respect the composition, creating new instances that will not leave out of this class
        this.customizedRequestList = new ArrayList<>();
        for(CustomizedRequest customizedRequest : customizedRequestList){
            CustomizedRequest newCustomizedRequest = new CustomizedRequest();
            newCustomizedRequest.setCustomizedDescription(customizedRequest.getCustomizedDescription());
            newCustomizedRequest.setRequestRelevance(customizedRequest.getRequestRelevance());
            this.customizedRequestList.add(newCustomizedRequest);
        }
    }

    public void setOrganizer(Organizer organizer) {
        this.organizer = organizer;
    }

    public void setCustomer(Customer customer){
        this.customer = customer;
    }


    public void setDiscount(float discount){
        this.discount = discount;
    }

    public void setCommissionFee(float commissionFee){
        this.commissionFee = commissionFee;
    }

    public void setSkateboard(SkateboardBase skateboard){
        this.skateboard = skateboard;
    }


    public void setBudget(int budget) {
        this.budget = budget;
    }


    public Organizer getOrganizer() {
        return organizer;
    }

    public Customer getCustomer() {
        return customer;
    }

    public SkateboardBase getSkateboard() {
        return skateboard;
    }

    public float getCommissionFee() {
        return commissionFee;
    }

    public float getDiscount() {
        return discount;
    }

    public int getBudget() {
        return budget;
    }

    public float calculateAllCost(){
        if(this.skateboard != null){
            return (skateboard.getPrice()+commissionFee)*discount;
        }
        return 0;
    }

    public String getCustomizationSummary() {
        String summary = "Commission Customizations:\n";

        for (CustomizedRequest request : this.customizedRequestList) {
            summary += "Description: " + request.getCustomizedDescription() +
                    ", Relevance: " + request.getRequestRelevance() + "\n";
        }

        return summary;
    }
}
