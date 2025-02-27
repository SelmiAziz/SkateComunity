package login;

public class User {
    private String name;
    private String surname;
    private Account account;

    public User(String name, String surname, Account account){
        this.name = name;
        this.surname = surname;
        this.account = account;
    }

    public User(String name, String surname){
        this.name = name;
        this.surname = surname;
    }

    public Account getAccount(){
        return this.account;
    }

    public void setAccount(Account account){
        this.account = account;
    }

    public String getName(){
        return this.name;
    }

    public String getSurname(){
        return this.surname;
    }

}


