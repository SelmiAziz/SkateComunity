package beans;

public class RegistrationRequestBean {
    private String registrationName;
    private String email;


    public RegistrationRequestBean(){
        //empty
    }

    public void setRegistrationName(String registrationName){
        this.registrationName = registrationName;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public String getRegistrationName(){
        return this.registrationName;
    }

    public String getEmail(){
        return this.email;
    }
}
