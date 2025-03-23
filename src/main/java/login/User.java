package login;

public class User {
    private String username;
    private String password;
    private String dateOfBirth;
    protected Role role;

    public User( String username, String password, String dateOfBirth, Role role){

        this.username = username;
        this.password = password;
        this.role = role;
        this.dateOfBirth = dateOfBirth;
    }

    public User(String username, String password, String dateOfBirth){
        this.username = username;
        this.password = password;
        this.dateOfBirth = dateOfBirth;
    }

    public Role getRole(){
        return this.role;
    }

    public void setRole(Role role){
        this.role = role;
    }


    public String getUsername(){return this.username;}

    public void setUsername(String username){
        this.username = username;
    }

    public String getPassword(){
        return this.password;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public String getDateOfBirth(){return this.dateOfBirth;}

    public void setDateOfBirth(String dateOfBirth){this.dateOfBirth = dateOfBirth;}








}


