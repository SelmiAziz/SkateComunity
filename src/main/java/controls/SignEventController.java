package controls;

import Dao.CostumerDao;
import Dao.EventDao;
import Dao.Factories.DaoFactory;
import beans.EventBean;
import exceptions.InsufficientCoinsException;
import exceptions.NoAvailableSeats;
import exceptions.UserAlreadySignedEvent;
import login.Account;
import model.Costumer;
import model.Event;
import model.EventParticipation;
import model.Organizer;
import utils.AccountInfoSessionManager;
import utils.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class SignEventController {
    private final DaoFactory daoFactory = DaoFactory.getInstance();
    private final EventDao eventDao = daoFactory.createEventDao();
    private final CostumerDao costumerDao = daoFactory.createCostumerDao();
    private final SessionManager sessionManager = SessionManager.getInstance();


    public EventBean eventDetails(EventBean eventBean){
        Event event = eventDao.getEventByName(eventBean.getName());
        return new EventBean(event.getName(), event.getDescription(), event.getCoins(),(event.getMaxRegistrations() - event.getCurrentRegistrations()));
    }

    public List<EventBean> allEvents(){
        List<Event> eventList = eventDao.getAllEvents();
        List<EventBean> eventBeanList = new ArrayList<>();
        for (Event event : eventList){
            eventBeanList.add(new EventBean(event.getName(),event.getDescription(), event.getDate()));
        }
        return eventBeanList;
    }

    public void signToEvent(EventBean eventBean) throws UserAlreadySignedEvent, InsufficientCoinsException, NoAvailableSeats {
        Account account = sessionManager.getSession().getAccount();
        Costumer currentCostumer = costumerDao.getUserByUsername(account.getUsername());
        Event event = eventDao.getEventByName(eventBean.getName());
        Organizer organizer = event.getOrganizer();
        List<EventParticipation> participantsList = event.getParticipants();
        for(EventParticipation eventParticipation : participantsList ){
            if(eventParticipation.getParticipant().getAccount().getUsername().equals(currentCostumer.getAccount().getUsername())){
                throw new UserAlreadySignedEvent("L'utente è già segnato a questo evento");
            }
        }
        if(currentCostumer.getAccount().getCoins() - event.getCoins() < 0){
            throw new InsufficientCoinsException("L'utente non disponde del numero di coins richieste");
        }
        if(!(event.getMaxRegistrations() - event.getCurrentRegistrations() >0)){
            throw new NoAvailableSeats("Non sono presenti più posti disponibili per tale evento");
        }
        currentCostumer.payCoins(event.getCoins());
        AccountInfoSessionManager.getInstance().getAccountInfo().decrementCoins(event.getCoins());
        event.addEventParticipation(new EventParticipation( event.getCoins()+1, currentCostumer));
        organizer.gainCoins(eventBean.getCoins());


    }

    public List<EventBean> searchEventByDateAndCountry(EventBean eventBean){
        List<EventBean> eventBeanList = new ArrayList<>();
        List<Event> eventList = eventDao.getEventsByDateAndCountry(eventBean.getDate(), eventBean.getCountry());
        for(Event event: eventList){
            eventBeanList.add(new EventBean(event.getName(), event.getDescription(), event.getDate()));
        }
        return eventBeanList;
    }

}
