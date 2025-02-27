package Dao;

import model.Costumer;

import java.util.List;

public interface UserDao {
    public void addUser(Costumer user);
    //Non ha molto senso
    public Costumer getUserByUsername(String username);
    public List<Costumer> getAllUsers();
}
