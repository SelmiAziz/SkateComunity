package model;

public class DeliveryPreferences {
    private String preferredTimeSlot;
    private String comment;

    public DeliveryPreferences(String preferredTimeSlot, String comment){
        this.preferredTimeSlot = preferredTimeSlot;
        this.comment = comment;
    }

    public String getComment() {
        return comment;
    }

    public String getPreferredTimeSlot() {
        return preferredTimeSlot;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setPreferredTimeSlot(String preferredTimeSlot) {
        this.preferredTimeSlot = preferredTimeSlot;
    }
}
