package Dao;

import model.Costumer;

import java.util.List;

public interface UserDao {
    public void addUser(Costumer user);
    public Costumer getUserByUsername(String username);
    public List<Costumer> getAllUsers();
}
