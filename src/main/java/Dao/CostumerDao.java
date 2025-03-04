package Dao;

import model.Costumer;

import java.util.List;

public interface CostumerDao {
    public void addCostumer(Costumer user);
    public Costumer getUserByUsername(String username);
    public List<Costumer> getAllUsers();
}
