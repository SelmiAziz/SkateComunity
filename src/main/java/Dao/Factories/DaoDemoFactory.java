package Dao.Factories;

import Dao.EventDao;
import Dao.EventDemoDao;
import model.Event;

public class DaoDemoFactory extends DaoFactory{
    public EventDao createEventDao(){
        return EventDemoDao.getInstance();
    }
}
