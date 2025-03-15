package controls;

import dao.EventDao;
import dao.patternAbstractFactory.DaoFactory;
import dao.OrganizerDao;
import dao.UserDao;
import beans.EventBean;
import beans.UserInfo;
import exceptions.EventAlreadyExistsException;
import login.User;
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
        Event newEvent = new Event(eventBean.getName(), eventBean.getDescription(), eventBean.getDate(), eventBean.getCountry(), eventBean.getCoins(),  eventBean.getMaxRegistrations());
        Organizer organizer = organizerDao.selectOrganizerByOrganizerName(SessionManager.getInstance().getSession().getNameProfile());
        newEvent.setOrganizer(organizer);
        organizer.addEvent(newEvent);
        eventDao.addEvent(newEvent);
    }


    public List<EventBean> organizerEvents(){
        Organizer organizer = organizerDao.selectOrganizerByOrganizerName(SessionManager.getInstance().getSession().getNameProfile());
        List<Event> eventList = organizer.getAllEvents();
        for(Event event : eventList){
            System.out.println("L'EVENTOO "+event.getName());
        }
        List<EventBean> eventBeanList = new ArrayList<>();
        for (Event event : eventList){
            eventBeanList.add(new EventBean(event.getName(),event.getDescription(), event.getDate(), event.getCountry(),event.getCoins(), event.getCurrentRegistrations(), event.getMaxRegistrations()));
        }
        return eventBeanList;
    }

    public UserInfo getCurrentUserInfo(){
        User user = userDao.selectUserByUsername(SessionManager.getInstance().getSession().getUsername());
        return new  UserInfo(user.getUsername(), user.getProfile().getCoins());
    }
}
