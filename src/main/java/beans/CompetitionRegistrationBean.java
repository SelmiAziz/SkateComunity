package beans;

public class CompetitionRegistrationBean {
    int currentRegistrationNumber;
    String assignedSeat;
    String registrationCode;

    public CompetitionRegistrationBean(int currentRegistrationNumber, String registrationCode, String assignedSeat){
        this.currentRegistrationNumber = currentRegistrationNumber;
        this.assignedSeat = assignedSeat;
        this.registrationCode = registrationCode;
    }

    public String getRegistrationCode() {
        return registrationCode;
    }

    public int getCurrentRegistrationNumber() {
        return currentRegistrationNumber;
    }

    public String getAssignedSeat() {
        return assignedSeat;
    }

    public void setCurrentRegistrationNumber(int currentRegistrationNumber) {
        this.currentRegistrationNumber = currentRegistrationNumber;
    }

    public void setRegistrationCode(String registrationCode) {
        this.registrationCode = registrationCode;
    }

    public void setAssignedSeat(String assignedSeat) {
        this.assignedSeat = assignedSeat;
    }
}
