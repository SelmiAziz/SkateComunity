package controls;

import dao.*;
import dao.patternAbstractFactory.DaoFactory;
import beans.EventBean;
import beans.UserInfo;
import exceptions.InsufficientCoinsException;
import exceptions.NoAvailableSeats;
import exceptions.UserAlreadySignedEvent;
import login.User;
import model.Costumer;
import model.Event;
import model.EventRegistration;
import model.Organizer;
import utils.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class SignEventController {
    private final DaoFactory daoFactory = DaoFactory.getInstance();
    private final EventDao eventDao = daoFactory.createEventDao();
    private final UserDao userDao = daoFactory.createUserDao();
    private final OrganizerDao organizerDao = daoFactory.createOrganizerDao();
    private final EventRegistrationDao eventRegistrationDao = daoFactory.createEventRegistrationDao();
    private final CostumerDao costumerDao = daoFactory.createCostumerDao();
    private final SessionManager sessionManager = SessionManager.getInstance();


    public EventBean eventDetails(EventBean eventBean){
        Event event = eventDao.selectEventByName(eventBean.getName());
        return new EventBean(event.getName(), event.getDescription(), event.getCoins(),(event.getMaxRegistrations() - event.getCurrentRegistrations()));
    }

    public List<EventBean> allAvailableEvents(){
        List<Event> eventList = eventDao.selectAvailableEvents();
        List<EventBean> eventBeanList = new ArrayList<>();
        for (Event event : eventList){
            eventBeanList.add(new EventBean(event.getName(),event.getDescription(), event.getDate()));
        }
        return eventBeanList;
    }

    public void signToEvent(EventBean eventBean) throws UserAlreadySignedEvent, InsufficientCoinsException, NoAvailableSeats {
        String costumerName = sessionManager.getSession().getNameProfile();
        Costumer currentCostumer = costumerDao.selectCostumerByCostumerName(costumerName);
        Event event = eventDao.selectEventByName(eventBean.getName());
        Organizer organizer = event.getOrganizer();
        List<EventRegistration> participantsList = event.getEventRegistrations();
        for(EventRegistration eventParticipation : participantsList ){
            if(eventParticipation.getParticipant().getName().equals(currentCostumer.getName())){
                throw new UserAlreadySignedEvent("L'utente è già segnato a questo evento");
            }
        }
        if(currentCostumer.getCoins() - event.getCoins() < 0){
            throw new InsufficientCoinsException("L'utente non disponde del numero di coins richieste");
        }
        if(event.getMaxRegistrations() - event.getCurrentRegistrations() <= 0){
            throw new NoAvailableSeats("Non sono presenti più posti disponibili per tale evento");
        }
        currentCostumer.payCoins(event.getCoins());

        EventRegistration newEventRegistration = new EventRegistration( event.getCurrentRegistrations()+1, currentCostumer);
        newEventRegistration.setEvent(event);
        event.addEventRegistration(newEventRegistration);
        eventRegistrationDao.addEventRegistration(newEventRegistration);
        organizer.gainCoins(eventBean.getCoins());
        costumerDao.update(currentCostumer);
        organizerDao.update(organizer);


    }

    public List<EventBean> searchEventByDateAndCountry(EventBean eventBean){
        List<EventBean> eventBeanList = new ArrayList<>();
        List<Event> eventList = eventDao.selectEventsByDateAndCountry(eventBean.getDate(), eventBean.getCountry());
        for(Event event: eventList){
            eventBeanList.add(new EventBean(event.getName(), event.getDescription(), event.getDate()));
        }
        return eventBeanList;
    }


    public UserInfo getCurrentUserInfo(){
        User user = userDao.selectUserByUsername(SessionManager.getInstance().getSession().getUsername());
        return new  UserInfo(user.getUsername(), user.getProfile().getCoins());
    }

}
