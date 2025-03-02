package Dao;

import model.Costumer;

import java.util.ArrayList;
import java.util.List;

public class UserDemoDao  implements CostumerDao {
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

    public Costumer getUserByUsername(String username){
        for(Costumer user:userList){
            if(user.getAccount().getUsername().equals(username)){
                return user;
          }
        }
        return null;
    }

}

