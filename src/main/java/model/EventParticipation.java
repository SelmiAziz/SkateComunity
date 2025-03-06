package model;

public class EventParticipation {
    private String participationDate;
    private int participationNumber;
    private Costumer participant;

    public EventParticipation(String participationDate, int participationNumber, Costumer participant){
        this.participationDate = participationDate;
        this.participationNumber = participationNumber;
        this.participant = participant;
    }

    public EventParticipation(int participationNumber, Costumer participant){
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

}
