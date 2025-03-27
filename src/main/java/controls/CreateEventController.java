package controls;

import dao.EventDao;
import dao.patternAbstractFactory.DaoFactory;
import dao.OrganizerDao;
import dao.UserDao;
import beans.EventBean;
import exceptions.EventAlreadyExistsException;
import model.Event;
import model.Organizer;
import utils.SessionManager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CreateEventController {
    private final DaoFactory daoFactory = DaoFactory.getInstance();
    private final EventDao eventDao = daoFactory.createEventDao();
    private final UserDao userDao = daoFactory.createUserDao();
    private final OrganizerDao organizerDao = daoFactory.createOrganizerDao();


    public void createEvent(EventBean eventBean) throws EventAlreadyExistsException, SQLException {
        String newEventName = eventBean.getName();
        eventDao.checkEvent(newEventName);
        if (eventDao.checkEvent(newEventName)) {
                throw new EventAlreadyExistsException("L'Evento in questione è già esiste");
        }
        Event newEvent = new Event(eventBean.getName(), eventBean.getDescription(), eventBean.getDate(), eventBean.getLocation(), eventBean.getCoins(),  eventBean.getMaxRegistrations());
        Organizer organizer = organizerDao.selectOrganizerByUsername(SessionManager.getInstance().getSession().getUsername());
        newEvent.setOrganizer(organizer);
        organizer.addEvent(newEvent);
        eventDao.addEvent(newEvent);
    }


    public List<EventBean> organizerEvents(){
        Organizer organizer = organizerDao.selectOrganizerByUsername(SessionManager.getInstance().getSession().getUsername());
        List<Event> eventList = organizer.getEventCreatedList();
        List<EventBean> eventBeanList = new ArrayList<>();
        for (Event event : eventList){
            eventBeanList.add(new EventBean(event.getName(),event.getDescription(), event.getDate(), event.getLocation(),event.getParticipationFee(), event.getCurrentRegistrations(), event.getMaxRegistrations()));
        }
        return eventBeanList;
    }

}
