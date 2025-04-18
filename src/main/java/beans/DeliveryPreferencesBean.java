package beans;

public class DeliveryPreferencesBean {
    private String comment;
    private String preferredTimeSlot;

    public DeliveryPreferencesBean(){}


    public String getPreferredTimeSlot() {
        return preferredTimeSlot;
    }

    public void setPreferredTimeSlot(String preferredTimeSlot) {
        this.preferredTimeSlot = preferredTimeSlot;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
