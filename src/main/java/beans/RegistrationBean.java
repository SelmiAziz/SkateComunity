package beans;

public class RegistrationBean {
    int currentRegistrationNumber;
    String assignedSeat;
    String registrationCode;

    public RegistrationBean(){
        //empty
    }

    public String getRegistrationCode() {
        return registrationCode;
    }


    public int getCurrentRegistrationNumber() {
        return this.currentRegistrationNumber;
    }

    public String getAssignedSeat() {
        return this.assignedSeat;
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
