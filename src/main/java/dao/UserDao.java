package dao;

import login.User;

public interface UserDao {
     User selectUserByUsername(String username);
     void addUser(User user);
     boolean checkUser(String username, String password);
}
