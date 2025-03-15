package model;

public class EventRegistration {
    private int registrationId;
    private String participationDate;
    private int participationNumber;
    private Costumer participant;
    private Event event;

    public EventRegistration(int registrationId , int participationNumber){
        this.registrationId = registrationId;
        this.participationNumber = participationNumber;
    }

    public EventRegistration(String participationDate, int participationNumber, Costumer participant){
        this.participationDate = participationDate;
        this.participationNumber = participationNumber;
        this.participant = participant;
    }

    public EventRegistration(int participationNumber, Costumer participant){
        this.participationNumber = participationNumber;
        this.participant = participant;
    }

    public String getParticipationDate(){
        return this.participationDate;
    }

    public void setParticipantDate(String participationDate){
        this.participationDate = participationDate;
    }

    public int getParticipationNumber(){
        return this.participationNumber;
    }

    public void setParticipationNumber(int participationNumber){
        this.participationNumber = participationNumber;
    }

    public Costumer getParticipant(){
        return this.participant;
    }

    public void setParticipant(Costumer participant){
        this.participant = participant;
    }

    public int getRegistrationId(){return this.registrationId;}

    public void setEvent(Event event){
        this.event = event;
    }

    public Event getEvent(){
        return this.event;
    }

}
