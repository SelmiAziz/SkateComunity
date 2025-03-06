package Dao;

import model.Event;

import java.util.List;

public interface EventDao {
    public Event getEventByName(String nameEvent);
    public void removeEventByName(String NameEvent);
    public void addEvent(Event event);
    public List<Event> getAllEvents();
    public List<Event> getEventsByDateAndCountry(String date, String country);
}
