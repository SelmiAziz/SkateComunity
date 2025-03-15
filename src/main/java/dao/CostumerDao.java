package dao;

import model.Costumer;

public interface CostumerDao {
    void addCostumer(Costumer user);
    Costumer selectCostumerByCostumerName(String username);
    void update(Costumer costumer);
}
