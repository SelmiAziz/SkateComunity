package Dao;

import model.Costumer;

import java.util.ArrayList;
import java.util.List;

public class UserDemoDao  implements UserDao{
    private static UserDemoDao instance;
    private List<Costumer> userList;
    private AccountDao accountDao;

    public UserDemoDao() {
        this.userList = new ArrayList<>();

    }

    public synchronized static UserDemoDao getInstance() {
        if (instance == null) {
            instance = new UserDemoDao();
        }
        return instance;
    }

    public List<Costumer> getAllUsers(){
        return this.userList;
    }

    public void addUser(Costumer user){
        this.userList.add(user);
    }

    //attento sta roba non mi piace
    public Costumer getUserByUsername(String username){
        for(Costumer user:userList){

        }
        return null;
    }

}

