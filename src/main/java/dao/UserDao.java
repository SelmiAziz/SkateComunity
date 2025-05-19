package dao;

import login.User;

public interface UserDao {
     User selectUserByUsername(String username);
     void addUser(User user);
     boolean checkUserByUsernameAndPassword(String username, String password);
     boolean checkUserByUsername(String username);
}
