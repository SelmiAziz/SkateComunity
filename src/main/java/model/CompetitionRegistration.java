package model;

public class CompetitionRegistration {
    private int registrationId;
    private int participationNumber;
    private Customer participant;
    private Competition competition;
    private String registrationCode;
    private String assignedTurn;

    public CompetitionRegistration(int registrationId , int participationNumber, String registrationCode, String assignedTurn){
        this.registrationId = registrationId;
        this.participationNumber = participationNumber;
        this.registrationCode = registrationCode;
        this.assignedTurn = assignedTurn;
    }



    public CompetitionRegistration(int participationNumber, String registrationCode, String assignedTurn){
        this.participationNumber = participationNumber;
        this.registrationCode = registrationCode;
        this.assignedTurn = assignedTurn;
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

    public void setCompetition(Competition competition){
        this.competition = competition;
    }

    public Competition getCompetition(){
        return this.competition;
    }

    public void setRegistrationId(int id){this.registrationId = id;}


    public void setAssignedTurn(String assignedTurn) {
        this.assignedTurn = assignedTurn;
    }

    public String getAssignedTurn() {
        return assignedTurn;
    }



    public void setRegistrationCode(String registrationCode) {
        this.registrationCode = registrationCode;
    }

    public String getRegistrationCode() {
        return registrationCode;
    }
}
