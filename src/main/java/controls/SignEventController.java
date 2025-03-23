package controls;

import dao.*;
import dao.patternAbstractFactory.DaoFactory;
import beans.EventBean;
import beans.UserInfo;
import exceptions.InsufficientCoinsException;
import exceptions.NoAvailableSeats;
import exceptions.UserAlreadySignedEvent;
import login.User;
import model.Customer;
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
    private final CustomerDao costumerDao = daoFactory.createCostumerDao();
    private final SessionManager sessionManager = SessionManager.getInstance();


    public EventBean eventDetails(EventBean eventBean){
        Event event = eventDao.selectEventByName(eventBean.getName());
        return new EventBean(event.getName(), event.getDescription(), event.getParticipationFee(),(event.getMaxRegistrations() - event.getCurrentRegistrations()));
    }

    public List<EventBean> allAvailableEvents(){
        List<Event> eventList = eventDao.selectAvailableEvents();
        List<EventBean> eventBeanList = new ArrayList<>();
        for (Event event : eventList){
            eventBeanList.add(new EventBean(event.getName(),event.getLocation(), event.getDate()));
        }
        return eventBeanList;
    }

    public void signToEvent(EventBean eventBean) throws UserAlreadySignedEvent, InsufficientCoinsException, NoAvailableSeats {
        String costumerName = sessionManager.getSession().getUsername();
        Customer currentCostumer = costumerDao.selectCustomerByUsername(costumerName);
        System.out.println(currentCostumer.getUsername()+currentCostumer.getDateOfBirth()+currentCostumer.getRole()+currentCostumer.getSkaterLevel());
        Event event = eventDao.selectEventByName(eventBean.getName());
        Organizer organizer = event.getOrganizer();
        List<EventRegistration> participantsList = event.getEventRegistrations();
        for(EventRegistration eventParticipation : participantsList ){
            if(eventParticipation.getParticipant().getUsername().equals(currentCostumer.getUsername())){
                throw new UserAlreadySignedEvent("L'utente è già segnato a questo evento");
            }
        }
        if(currentCostumer.getCoins() - event.getParticipationFee() < 0){
            throw new InsufficientCoinsException("L'utente non disponde del numero di coins richieste");
        }
        if(event.getMaxRegistrations() - event.getCurrentRegistrations() <= 0){
            throw new NoAvailableSeats("Non sono presenti più posti disponibili per tale evento");
        }
        //I have to fink about this
        currentCostumer.pay(event.getParticipationFee());

        EventRegistration newEventRegistration = new EventRegistration( event.getCurrentRegistrations()+1, currentCostumer);
        newEventRegistration.setEvent(event);
        event.addEventRegistration(newEventRegistration);
        eventRegistrationDao.addEventRegistration(newEventRegistration);


    }

    public List<EventBean> searchEventByDateAndLocation(EventBean eventBean){
        List<EventBean> eventBeanList = new ArrayList<>();
        List<Event> eventList = eventDao.selectEventsByDateAndLocation(eventBean.getDate(), eventBean.getLocation());
        for(Event event: eventList){
            eventBeanList.add(new EventBean(event.getName(), event.getLocation(), event.getDate()));
        }
        return eventBeanList;
    }



}
