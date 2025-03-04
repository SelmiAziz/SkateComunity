package Dao;

import model.Costumer;

import java.util.ArrayList;
import java.util.List;

public class CostumerDemoDao implements CostumerDao {
    private static CostumerDemoDao instance;
    private List<Costumer>  costumerList;
    private AccountDao accountDao;

    public CostumerDemoDao() {
        this.costumerList = new ArrayList<>();

    }

    public synchronized static CostumerDemoDao getInstance() {
        if (instance == null) {
            instance = new CostumerDemoDao();
        }
        return instance;
    }

    public List<Costumer> getAllUsers(){
        return this.costumerList;
    }

    public void addCostumer(Costumer costumer){
        this.costumerList.add(costumer);
    }

    public Costumer getUserByUsername(String username){
        for(Costumer costumer:costumerList){
            if(costumer.getAccount().getUsername().equals(username)){
                return costumer;
          }
        }
        return null;
    }

}

