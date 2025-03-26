package model;

public class EventRegistration {
    private int registrationId;
    private int participationNumber;
    private Customer participant;
    private Event event;
    private String registrationCode;
    private String assignedSeat;

    public EventRegistration(int registrationId , int participationNumber, String registrationCode, String assignedSeat){
        this.registrationId = registrationId;
        this.participationNumber = participationNumber;
        this.registrationCode = registrationCode;
        this.assignedSeat = assignedSeat;
    }



    public EventRegistration(int participationNumber,  String registrationCode, String assignedSeat){
        this.participationNumber = participationNumber;
        this.registrationCode = registrationCode;
        this.assignedSeat = assignedSeat;
    }



    public int getParticipationNumber(){
        return this.participationNumber;
    }

    public void setParticipationNumber(int participationNumber){
        this.participationNumber = participationNumber;
    }

    public Customer getParticipant(){
        return this.participant;
    }

    public void setParticipant(Customer participant){
        this.participant = participant;
    }

    public int getRegistrationId(){return this.registrationId;}

    public void setEvent(Event event){
        this.event = event;
    }

    public Event getEvent(){
        return this.event;
    }

    public void setRegistrationId(int id){this.registrationId = id;}


    public void setAssignedSeat(String assignedSeat) {
        this.assignedSeat = assignedSeat;
    }

    public String getAssignedSeat() {
        return assignedSeat;
    }



    public void setRegistrationCode(String registrationCode) {
        this.registrationCode = registrationCode;
    }

    public String getRegistrationCode() {
        return registrationCode;
    }
}
