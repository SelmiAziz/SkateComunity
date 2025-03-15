package Dao;

import login.User;

import java.util.List;

public interface UserDao {
     User selectUserByUsername(String username);
     void addUser(User user);
     boolean checkUser(String username, String password);
}
