package dao;

import model.Costumer;

import java.util.ArrayList;
import java.util.List;

public class CostumerDemoDao implements CostumerDao {
    private static CostumerDemoDao instance;
    private final List<Costumer>  costumerList;

    public CostumerDemoDao() {
        this.costumerList = new ArrayList<>();

    }

    public static synchronized  CostumerDemoDao getInstance() {
        if (instance == null) {
            instance = new CostumerDemoDao();
        }
        return instance;
    }

    public void addCostumer(Costumer costumer){
        this.costumerList.add(costumer);
    }

    public Costumer selectCostumerByCostumerName(String costumerName){
        for(Costumer costumer:costumerList){
            if(costumer.getName().equals(costumerName)){
                return costumer;
          }
        }
        return null;
    }

    @Override
    public void update(Costumer costumer) {
        //Not needed in demo
    }
}

