package beans;


public class LogUserBean {
    private String username;
    private String password;
    private String role;

    public LogUserBean(String username, String password, String role){
        this.username = username;
        this.password = password;
        this.role = role;
    }


    public LogUserBean(String username, String password){
        this.username = username;
        this.password = password;
    }

    public LogUserBean(String role){
        this.role = role;
    }

    public String getRole(){return this.role;}

    public String getUsername(){
        return this.username;
    }

    public String getPassword(){
        return this.password;
    }


    public void setUsername(String username){
        this.username = username;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public void setRole(String role){this.role = role;}



}
