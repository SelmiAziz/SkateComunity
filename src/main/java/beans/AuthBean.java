package beans;

public class AuthBean {
    String token;
    String role;

    public AuthBean(String token, String role){
        this.token = token;
        this.role = role;
    }

    public String getToken(){
        return this.token;
    }

    public String getRole(){
        return this.role;
    }
}
