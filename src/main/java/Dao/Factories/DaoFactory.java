package Dao.Factories;

import Dao.EventDao;
import model.Event;

abstract public class DaoFactory {
    public abstract EventDao createEventDao();

    private static DaoFactory instance;

    public synchronized static DaoFactory getInstance(){
        String prop = "demo";
        if(prop.equals("demo")){
            instance = new DaoDemoFactory();
        }
        return instance;
    }
}
