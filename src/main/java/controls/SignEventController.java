package controls;

import Dao.CostumerDao;
import Dao.EventDao;
import Dao.Factories.DaoFactory;
import beans.EventBean;
import exceptions.InsufficientCoinsException;
import exceptions.UserAlreadySignedEvent;
import login.Account;
import model.Costumer;
import model.Event;
import utils.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class SignEventController {
    private final DaoFactory daoFactory = DaoFactory.getInstance();
    private final EventDao eventDao = daoFactory.createEventDao();
    private final CostumerDao costumerDao = daoFactory.createCostumerDao();
    private final SessionManager sessionManager = SessionManager.getInstance();


    public EventBean showEventDetail(String eventName){
        Event event = eventDao.getEventByName(eventName);
        return new EventBean(event.getName(), event.getDescription(), (event.getMaxRegistrations() - event.getCurrentRegistrations()), event.getCoins());
    }

    public List<EventBean> showAllEvents(){
        List<Event> eventList = eventDao.getAllEvents();
        List<EventBean> eventBeanList = new ArrayList<>();
        for (Event event : eventList){
            eventBeanList.add(new EventBean(event.getName(),event.getDescription(), event.getDate()));
        }
        return eventBeanList;
    }

    public void signEvent(EventBean eventBean) throws UserAlreadySignedEvent {
        Account account = sessionManager.getSession().getAccount();
        Costumer currentCostumer = costumerDao.getUserByUsername(account.getUsername());
        Event event = eventDao.getEventByName(eventBean.getName());
        List<Costumer> costumerList = event.getParticipants();
        for(Costumer costumer : costumerList ){
            if(costumer.getAccount().getUsername().equals(currentCostumer.getAccount().getUsername())){
                throw new UserAlreadySignedEvent("L'utente è già segnato a questo evento");
            }
        }
        if(currentCostumer.getAccount().getCoins() - event.getCoins() < 0){
            throw new InsufficientCoinsException("L'utente non disponde del numero di coins richieste");
        }
        currentCostumer.getAccount().pay(event.getCoins());
        System.out.println("Hai pagato");
        event.addParticipant(currentCostumer);

    }

}
