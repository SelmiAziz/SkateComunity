package dao;

import login.User;

import java.util.ArrayList;
import java.util.List;

public class UserDemoDao implements UserDao {
    private static UserDemoDao instance;
    private final List<User> userList;

    public UserDemoDao() {
        this.userList = new ArrayList<>();

    }

    public static synchronized  UserDemoDao getInstance() {
        if (instance == null) {
            instance = new UserDemoDao();
        }
        return instance;
    }

    @Override
    public void addUser(User user){
        this.userList.add(user);
    }

    @Override
    public User selectUserByUsername(String username){
        for( User user: this.userList){
            if(user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }


    @Override
    public boolean checkUserByUsernameAndPassword(String username, String password) {
        for(User user: userList){
            if(user.getUsername().equals(username) && user.getPassword().equals(password) ){
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean checkUserByUsername(String username){
        for(User user:userList){
            if(user.getUsername().equals(username)){
                return true;
            }
        }
        return false;
    }

    public List<User> getUserList() {
        return userList;
    }
}
