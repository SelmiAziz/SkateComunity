package dao;

import login.User;

import java.io.IOException;

public interface UserDao {
     User selectUserByUsername(String username) throws IOException;
     void addUser(User user);
     boolean checkUserByUsernameAndPassword(String username, String password) throws IOException;
     boolean checkUserByUsername(String username) throws IOException;
}
