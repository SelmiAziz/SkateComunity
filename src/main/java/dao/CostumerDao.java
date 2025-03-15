package Dao;

import model.Costumer;

import java.util.List;

public interface CostumerDao {
    void addCostumer(Costumer user);
    Costumer selectCostumerByCostumerName(String username);
    void update(Costumer costumer);
}
