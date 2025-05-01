package beans;

public class RegisterUserBean {
    private String username;
    private String password;
    private String role;
    private String dateOfBirth;
    private String skillLevel;

    public RegisterUserBean(String username, String password, String role, String dateOfBirth, String skillLevel){

        this.username = username;
        this.password = password;
        this.role = role;
        this.dateOfBirth = dateOfBirth;
        this.skillLevel = skillLevel;

    }


    public void setUsername(String username){this.username = username;}

    public void setPassword(String password){this.password = password;}

    public void setRole(String role){this.role = role;}

    public void setSkillLevel(String skillLevel){this.skillLevel = skillLevel;}

    public void setDateOfBirth(String dateOfBirth){this.dateOfBirth = dateOfBirth;}




    public String getUsername(){
        return this.username;
    }

    public String getRole(){
        return this.role;
    }

    public String getPassword(){
        return this.password;
    }

    public String getDateOfBirth(){
        return this.dateOfBirth;
    }

    public String getSkillLevel(){
        return this.skillLevel;
    }


}
