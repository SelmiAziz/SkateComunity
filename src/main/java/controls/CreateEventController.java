package controls;

import Dao.EventDao;
import Dao.Factories.DaoFactory;
import Dao.OrganizerDao;
import beans.EventBean;
import exceptions.EventAlreadyExistsException;
import model.Event;
import model.Organizer;
import utils.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class CreateEventController {
    private final DaoFactory daoFactory = DaoFactory.getInstance();
    private final EventDao eventDao = daoFactory.createEventDao();
    private final OrganizerDao organizerDao = daoFactory.createOrganizerDao();




    public void createEvent(EventBean eventBean) throws EventAlreadyExistsException {
        List<Event> eventList = eventDao.getAllEvents();
        Event newEvent;
        Organizer organizer;
        String newEventName = eventBean.getName();
        for (Event event : eventList) {
            if (event.getName().equals(newEventName)) {
                throw new EventAlreadyExistsException("L'Evento in questione è già esiste");
            }
        }
        newEvent = new Event(eventBean.getName(), eventBean.getDescription(), eventBean.getDate(), eventBean.getCountry(), eventBean.getCoins(), eventBean.getMaxRegistrations());
        organizer = organizerDao.getOrganizerByUsername(SessionManager.getInstance().getSession().getAccount().getUsername());
        newEvent.setOrganizer(organizer);
        eventDao.addEvent(newEvent);
    }


    public List<EventBean> showAllEvents(){
        List<Event> eventList = eventDao.getAllEvents();
        List<EventBean> eventBeanList = new ArrayList<>();
        for (Event event : eventList){
            eventBeanList.add(new EventBean(event.getName(),event.getDescription(), event.getDate(), event.getCountry(),event.getCoins(), event.getMaxRegistrations()));
        }
        return eventBeanList;
    }
}
